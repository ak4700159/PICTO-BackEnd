# /tag api 테스트용

import os
from flask import Flask, request, jsonify
from flask_cors import CORS
import logging
import requests
import json
import io
from get_tag import tagging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

STORE_SERVER_URL = os.getenv('STORE_SERVER_URL', 'http://store-service:8084/photo-store/photos')

@app.route('/tag', methods=['POST'])
def process_image():
    logger.info("이미지 태깅 요청 받음")
    
    if 'image' not in request.files:
        logger.error("이미지 파일이 요청에 없음")
        return jsonify({'error': '이미지가 없습니다'}), 400

    file = request.files['image']
    original_data = request.form.get('data', '{}')
    
    try:
        image_data = file.read()
        image_stream = io.BytesIO(image_data)
        
        tag = tagging(image_stream)
        logger.info(f"생성된 태그: {tag}")

        try:
            data_dict = json.loads(original_data)
        except json.JSONDecodeError:
            data_dict = {}
        
        data_dict['tag'] = tag

        files = {
            'file': (file.filename, image_stream, 'image/jpeg')
        }

        form_data = {
            'request': json.dumps(data_dict)
        }
        
        response = requests.post(STORE_SERVER_URL, files=files, data=form_data)
        response.raise_for_status()
        
        return jsonify({
            'success': True,
            'tags': tag,
            'nodejs_response': response.json()
        }), 200

    except Exception as e:
        logger.error(f"처리 중 에러 발생: {str(e)}")
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8083, debug=True)