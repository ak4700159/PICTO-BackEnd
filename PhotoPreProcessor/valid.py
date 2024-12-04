from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import logging
import requests
from person_detector import PersonDetector
from nsfw_detector import NSFWDetector
from text_detector import TextDetector

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

UPLOAD_FOLDER = './uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

TAGGING_SERVICE_URL = os.getenv('TAGGING_SERVICE_URL', 'http://host.docker.internal:8083/tag')

def validate(image_path):
    logger.info(f"이미지 검증 시작: {image_path}")
    
    nsfw_detector = NSFWDetector(threshold=0.5)
    person_detector = PersonDetector('./person_detection.pth')
    text_detector = TextDetector()

    if nsfw_detector.detect(image_path):
        logger.warning("NSFW 콘텐츠 감지됨")
        return "nsfw"
    elif text_detector.detect(image_path):
        logger.warning("텍스트 감지됨")
        return "text"
    elif person_detector.detect(image_path):
        logger.warning("사람 감지됨")
        return "person"
    
    logger.info("검증 통과")
    return False

@app.route('/validate', methods=['POST'])
def validate_image():
    logger.info("이미지 검증 요청 받음")
    
    if 'image' not in request.files:
        logger.error("이미지 파일이 요청에 없음")
        return jsonify({'error': '이미지가 없습니다'}), 400

    file = request.files['image']
    original_data = request.form.get('data')
    
    try:
        temp_path = os.path.join(UPLOAD_FOLDER, file.filename)
        file.save(temp_path)
        logger.info(f"이미지 임시 저장됨: {temp_path}")

        error = validate(temp_path)
        if error:
            logger.warning(f"유효성 검사 실패: {error}")
            os.remove(temp_path)
            return jsonify({'error': error}), 400

        with open(temp_path, 'rb') as img_file:
            files = {'image': (file.filename, img_file, 'image/jpeg')}
            data = {'data': original_data} if original_data else {}
            
            response = requests.post(TAGGING_SERVICE_URL, files=files, data=data)
            
        os.remove(temp_path)
        logger.info("임시 파일 삭제됨")

        if response.status_code == 200:
            return response.json(), 200
        else:
            return jsonify({'error': '태깅 서비스 오류'}), 500

    except Exception as e:
        logger.error(f"처리 중 에러 발생: {str(e)}")
        if os.path.exists(temp_path):
            os.remove(temp_path)
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)