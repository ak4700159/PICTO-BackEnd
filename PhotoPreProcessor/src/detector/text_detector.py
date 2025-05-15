import easyocr
import cv2


class TextDetector:
    def __init__(self):
        self.languages = ['en', 'ko']
        self.reader = easyocr.Reader(self.languages)

    def detect(self, image_path):
        image = cv2.imread(image_path)
        image_area = image.shape[0] * image.shape[1]

        results = self.reader.readtext(image_path)

        text_area = 0
        for (bbox, text, confidence) in results:
            # bbox는 4개의 꼭짓점으로 구성된 리스트 [(x1, y1), (x2, y2), (x3, y3), (x4, y4)]
            # 바운딩 박스를 직사각형으로 근사 (axis-aligned bounding box)
            x_coords = [pt[0] for pt in bbox]
            y_coords = [pt[1] for pt in bbox]
            min_x, max_x = min(x_coords), max(x_coords)
            min_y, max_y = min(y_coords), max(y_coords)
            width = max_x - min_x
            height = max_y - min_y
            text_area += width * height

        # 비율 계산
        coverage_ratio = text_area / image_area if image_area > 0 else 0

        return {
            "has_text": len(results) > 0,
            "text_count": len(results),
            "text_coverage_ratio": coverage_ratio,  # 0~1 사이의 값
            "raw_results": results
        }
