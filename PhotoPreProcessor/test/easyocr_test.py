
import sys
import os

# [사람, 유해사진, 텍스트] 탐지 모델, 태깅 기능 임포트
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from src.detector.text_detector import TextDetector 
import easyocr
import cv2

reader = easyocr.Reader(['en', 'ko'])
def detect(image_path):
    image = cv2.imread(image_path)
    if image is None:
        print(f"[TextDetector] 이미지 로드 실패: {image_path}")
        return 0.0  # 또는 return (0.0, 0.0) 형태도 가능

    image_area = image.shape[0] * image.shape[1]
    try:
        print("DEBUG")
        print("image path : " + image_path)
        results = reader.readtext(image_path)
        if len(results) > 0:
            print("INFO: __main__: result :: ", results)
        else:
            return 0.0
    except Exception as e:
        print(f"[TextDetector] OCR 실패: {e}")
        return 0.0

    text_area = 0
    confidence_sum = 0.0
    valid_count = 0

    for res in results:
        print("DEBUG2")
        if len(res) != 3:
            continue
        bbox, text, confidence = res

        # confidence 필터링 (선택 사항)
        if confidence < 0.2:
            continue

        try:
            x_coords = [pt[0] for pt in bbox]
            print(x_coords)
            y_coords = [pt[1] for pt in bbox]
            print(y_coords)
            min_x, max_x = min(x_coords), max(x_coords)
            min_y, max_y = min(y_coords), max(y_coords)
            width = max_x - min_x
            height = max_y - min_y
            text_area += width * height

            confidence_sum += confidence
            valid_count += 1
        except Exception as e:
            print(f"[TextDetector] 박스 처리 오류: {e}")
            continue

    coverage_ratio = text_area / image_area if image_area > 0 else 0
    average_confidence = confidence_sum / valid_count if valid_count > 0 else 0.0

    # 최종 score는 비율과 confidence를 곱한 형태로 반환할 수도 있음
    score = coverage_ratio * average_confidence

    print(f"[TextDetector] text_area_ratio: {coverage_ratio:.3f}, average_confidence: {average_confidence:.3f}, score: {score:.3f}")
    return score


print(detect('./test1.jpg'))