import io
import json
from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import logging
import requests
import mimetypes
from person_detector import PersonDetector
from nsfw_detector import NSFWDetector
from text_detector import TextDetector
# from get_tag import tagging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

UPLOAD_FOLDER = './uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

STORE_SERVER_URL = 'http://52.78.237.242:8084/photo-store/photos'

def validate(image_path):    
    nsfw_detector = NSFWDetector(threshold=0.5)
    person_detector = PersonDetector('./person_detection.pth')
    text_detector = TextDetector()

    if nsfw_detector.detect(image_path):
        logger.warning("NSFW content detected")
        return "nsfw"
    elif text_detector.detect(image_path):
        logger.warning("Text detected")
        return "text"
    elif person_detector.detect(image_path):
        logger.warning("Person detected")
        return "person"
    
    logger.info("Validation passed")
    return False

@app.route('/validate', methods=['POST'])
def validate_image():
    logger.info("Image validation request received")
    
    if 'file' not in request.files:
        logger.error("No image file in request")
        return jsonify({'error': 'No image file'}), 400
    
    file = request.files['file']
    request_data = request.form.get('request', '{}')
    
    try:
        temp_path = os.path.join(UPLOAD_FOLDER, file.filename)
        file.save(temp_path)
        logger.info(f"Image temporarily saved: {temp_path}")
        
        # 파일 확장자에 따른 MIME 타입 결정
        mime_type = None
        if file.filename.lower().endswith(('.jpg', '.jpeg')):
            mime_type = 'image/jpeg'
        elif file.filename.lower().endswith('.png'):
            mime_type = 'image/png'
        elif file.filename.lower().endswith('.gif'):
            mime_type = 'image/gif'
        elif file.filename.lower().endswith('.webp'):
            mime_type = 'image/webp'
        else:
            logger.error("Unsupported image format")
            return jsonify({'error': '지원하지 않는 이미지 형식입니다.'}), 400

        logger.info(f"Determined MIME type: {mime_type}")
        
        error = validate(temp_path)
        if error:
            logger.warning(f"Validation failed: {error}")
            os.remove(temp_path)
            return jsonify({'error': error}), 400

        with open(temp_path, 'rb') as img_file:
            image_stream = io.BytesIO(img_file.read())
            tag = tagging(image_stream)
            logger.info(f"Generated tags: {tag}")
        
        data_dict = json.loads(request_data)
        data_dict['tag'] = tag

        # multipart 요청을 위한 boundary 설정
        boundary = 'boundary123456'
        headers = {
            'Content-Type': f'multipart/form-data; boundary={boundary}'
        }

        # Spring Boot 서버로 전송
        with open(temp_path, 'rb') as img_file:
            # multipart 형식으로 데이터 구성
            body = io.BytesIO()
            
            # 파일 파트 추가
            body.write(f'--{boundary}\r\n'.encode('utf-8'))
            body.write(f'Content-Disposition: form-data; name="file"; filename="{file.filename}"\r\n'.encode('utf-8'))
            body.write(f'Content-Type: {mime_type}\r\n\r\n'.encode('utf-8'))
            body.write(img_file.read())
            body.write(b'\r\n')
            
            # JSON 데이터 파트 추가
            body.write(f'--{boundary}\r\n'.encode('utf-8'))
            body.write('Content-Disposition: form-data; name="request"\r\n'.encode('utf-8'))
            body.write('Content-Type: application/json\r\n\r\n'.encode('utf-8'))
            body.write(json.dumps(data_dict, ensure_ascii=False).encode('utf-8'))
            body.write(b'\r\n')
            
            # 종료 바운더리 추가
            body.write(f'--{boundary}--\r\n'.encode('utf-8'))
            
            body_data = body.getvalue()
            
            logger.info(f"Sending request to Spring Boot server")
            logger.info(f"Content-Type: {headers['Content-Type']}")
            
            response = requests.post(
                STORE_SERVER_URL,
                data=body_data,
                headers=headers
            )
            response.raise_for_status()

        os.remove(temp_path)
        logger.info("Temporary file deleted")
        
        return jsonify(response.json()), 200
        
    except Exception as e:
        logger.error(f"Error during processing: {str(e)}")
        if os.path.exists(temp_path):
            os.remove(temp_path)
        return jsonify({'error': str(e)}), 500
    
@app.route('/tag', methods=['POST'])
def process_image():
    logger.info("Image tagging request received")
    
    if 'file' not in request.files:
        logger.error("No image file in request")
        return jsonify({'error': 'No image file'}), 400

    file = request.files['file']
    request_data = request.form.get('request', '{}')
    
    try:
        # Process image for tagging
        image_data = file.read()
        image_stream = io.BytesIO(image_data)
        tag = tagging(image_stream)
        logger.info(f"Generated tags: {tag}")

        # Update request data with tags
        try:
            data_dict = json.loads(request_data)
        except json.JSONDecodeError:
            data_dict = {}
        
        data_dict['tag'] = tag

        # Prepare multipart form data
        files = {
            'file': (file.filename, io.BytesIO(image_data), file.content_type or 'application/octet-stream')
        }
        form_data = {
            'request': json.dumps(data_dict, ensure_ascii=False)
        }
        
        # Forward to storage server
        response = requests.post(STORE_SERVER_URL, files=files, data=form_data)
        response.raise_for_status()
        
        return jsonify(response.json()), 200

    except Exception as e:
        logger.error(f"Error during processing: {str(e)}")
        return jsonify({'error': str(e)}), 500
    
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8083, debug=True)