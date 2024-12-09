import io
import json
from wsgiref import headers
from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import logging
import requests
import mimetypes

from shapely import boundary
from person_detector import PersonDetector
from nsfw_detector import NSFWDetector
from text_detector import TextDetector
from get_tag import tagging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

UPLOAD_FOLDER = './uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

StoreUrl = 'http://52.78.237.242:8084/photo-store/photos'

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
        
        # MIME 타입 결정
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
        
        # 이미지 검증
        error = validate(temp_path)
        if error:
            os.remove(temp_path)
            return jsonify({'error': error}), 400

        # 태깅
        with open(temp_path, 'rb') as img_file:
            image_stream = io.BytesIO(img_file.read())
            tag = tagging(image_stream)
            logger.info(f"Generated tags: {tag}")
            
        data_dict = json.loads(request_data)
        data_dict['tag'] = tag

        print(data_dict)
        # frameActive에 따른 처리
        if data_dict.get('frameActive', True):
            photo_id = data_dict.get('photoId')
            if not photo_id:
                return jsonify({'error': 'photoId is required'}), 400
            
            ServerUrl = f"{StoreUrl}/frame/{photo_id}"
        else:
            ServerUrl = StoreUrl

        boundary = 'boundary123456'
        headers = {
            'Content-Type': f'multipart/form-data; boundary={boundary}'
        }

        if data_dict.get('frameActive', True):
            photo_id = data_dict.get('photoId')
            if not photo_id:
                return jsonify({'error': 'photoId is required'}), 400
            ServerUrl = f"{StoreUrl}/frame/{photo_id}"


        # 요청 준비
        with open(temp_path, 'rb') as img_file:
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
            
            logger.info(f"Sending {'PATCH' if data_dict.get('frameActive', True) else 'POST'} request to Spring Boot server")
            logger.info(f"Content-Type: {headers['Content-Type']}")
            
            if data_dict.get('frameActive', True):
                print("Frame")
                data_dict['frameActive'] = False
                response = requests.patch(
                    ServerUrl,
                    data=body_data,
                    headers=headers
                )
            else:
                response = requests.post(
                    ServerUrl,
                    data=body_data,
                    headers=headers
                )
            
            response.raise_for_status()
            return jsonify(response.json()), 200
            
    except Exception as e:
        logger.error(f"Error during processing: {str(e)}")
        return jsonify({'error': str(e)}), 500
        
    finally:
        # 임시 파일 삭제
        if temp_path and os.path.exists(temp_path):
            try:
                os.remove(temp_path)
                logger.info("Temporary file deleted")
            except Exception as e:
                logger.error(f"Error deleting temporary file: {str(e)}")

@app.route('/tag', methods=['POST'])
def tag_image():
    logger.info("Image tagging request received")
    
    if 'file' not in request.files:
        logger.error("No image file in request")
        return jsonify({'error': 'No image file'}), 400

    file = request.files['file']
    request_data = request.form.get('request', '{}')
    temp_path = None
    
    try:
        # 임시 파일 저장
        temp_path = os.path.join(UPLOAD_FOLDER, file.filename)
        file.save(temp_path)
        
        # 이미지 태깅
        with open(temp_path, 'rb') as img_file:
            image_stream = io.BytesIO(img_file.read())
            tag = tagging(image_stream)
            logger.info(f"Generated tags: {tag}")

        data_dict = json.loads(request_data)
        data_dict['tag'] = tag

        # MIME 타입 결정
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

        # frameActive에 따른 처리
        if data_dict.get('frameActive', True):
            photo_id = data_dict.get('photoId')
            if not photo_id:
                return jsonify({'error': 'photoId is required'}), 400
                    
            ServerUrl = f"{StoreUrl}/frame/{photo_id}"
            
            # 원본 frameActive 값을 보관
            is_frame_request = True  # HTTP 메소드 결정용
            
            # 전송할 데이터의 복사본 생성
            request_data_dict = data_dict.copy()
            request_data_dict['frameActive'] = False  # 전송할 데이터에서만 False로 변경
            
            print("Original request data:", data_dict)  # 디버깅용
            print("Modified request data:", request_data_dict)  # 디버깅용

            # 요청 준비
            with open(temp_path, 'rb') as img_file:
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
                body.write(json.dumps(request_data_dict, ensure_ascii=False).encode('utf-8'))
                body.write(b'\r\n')
                
                # 종료 바운더리 추가
                body.write(f'--{boundary}--\r\n'.encode('utf-8'))
                
                body_data = body.getvalue()
                
                # is_frame_request를 기반으로 HTTP 메소드 결정
                if is_frame_request:
                    print("Sending PATCH request")
                    response = requests.patch(
                        ServerUrl,
                        data=body_data,
                        headers=headers
                    )
                else:
                    print("Sending POST request")
                    response = requests.post(
                        ServerUrl,
                        data=body_data,
                        headers=headers
                    )
            
            response.raise_for_status()
            return jsonify(response.json()), 200
            
    except Exception as e:
        logger.error(f"Error during processing: {str(e)}")
        return jsonify({'error': str(e)}), 500
        
    finally:
        # 임시 파일 삭제
        if temp_path and os.path.exists(temp_path):
            try:
                os.remove(temp_path)
                logger.info("Temporary file deleted")
            except Exception as e:
                logger.error(f"Error deleting temporary file: {str(e)}")


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8083, debug=True)  