import easyocr
import cv2
from PIL import Image
import io


class TextDetector:
    def __init__(self):
        self.reader = easyocr.Reader(['en', 'ko'])  # GPU 사용 시: gpu=True

    def detect(self, image_path):
        try:
            with open(image_path, "rb") as f:
                image_bytes = f.read()  # 파일 전체를 한 번만 읽음
        except Exception as e:
            print(f"[TextDetector] 파일 읽기 실패: {e}")
            return 0.0

        # 이미지 크기 계산
        try:
            image = Image.open(io.BytesIO(image_bytes))
            width, height = image.size
            image_area = width * height
            # print(f"[TextDetector] image size: {width} * {height}")
        except Exception as e:
            print(f"[TextDetector] 이미지 열기 실패: {e}")
            return 0.0

        # OCR 실행
        try:
            results = self.reader.readtext(image_bytes, paragraph=True)
            if not results:
                print("[TextDetector] 텍스트 없음")
                return 0.0
            # print("INFO: __main__: [text detect] ", results)
        except Exception as e:
            print(f"[TextDetector] OCR 실패: {e}")
            return 0.0

        # 텍스트 영역 계산
        text_area = 0
        confidence_sum = 0.0
        valid_count = 0

        for res in results:
            # if not isinstance(res, (list, tuple)) or len(res) != 3:
            #     print(f"[TextDetector] 잘못된 결과 형식: {res}")
            #     continue

            bbox, text = res

            # if confidence < 0.2:
            #     continue  # 너무 낮은 confidence는 무시

            try:
                x_coords = [pt[0] for pt in bbox]
                y_coords = [pt[1] for pt in bbox]
                min_x, max_x = min(x_coords), max(x_coords)
                min_y, max_y = min(y_coords), max(y_coords)
                width = max_x - min_x
                height = max_y - min_y
                text_area += width * height

                # confidence_sum += confidence
                valid_count += 1
            except Exception as e:
                print(f"[TextDetector] 박스 처리 오류: {e}, bbox = {bbox}")
                continue

        coverage_ratio = text_area / image_area if image_area > 0 else 0
        # average_confidence = confidence_sum / valid_count if valid_count > 0 else 0.0
        # score = coverage_ratio * average_confidence

        # print(f"[TextDetector] text_area_ratio: {coverage_ratio:.3f}, "
        #       f"average_confidence: {average_confidence:.3f}, score: {score:.3f}")
        return coverage_ratio * 2
