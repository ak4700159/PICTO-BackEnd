from ultralytics import YOLO
from PIL import Image

class PersonDetector:
    def __init__(self, model_path="./person_detection.pt", threshold=0.5):
        self.model = YOLO(model_path)
        self.threshold = threshold  # 필요 시 설정

    def detect(self, image_path):
        try:
            results = self.model.predict(source=image_path, imgsz=640, conf=0.25, save=False, verbose=False)

            # 결과에서 'person' 클래스 탐지 개수 추출 (클래스 ID가 0)
            person_count = 0
            max_conf = 0.0

            for result in results:
                if hasattr(result, 'boxes'):
                    for box in result.boxes:
                        cls_id = int(box.cls[0].item()) if hasattr(box.cls[0], 'item') else int(box.cls[0])
                        conf = float(box.conf[0]) if hasattr(box.conf[0], 'item') else float(box.conf[0])
                        if cls_id == 0:  # 클래스 0은 'person'
                            person_count += 1
                            max_conf = max(max_conf, conf)

            # 감지된 사람 중 가장 높은 confidence 반환 (없으면 0.0)
            return max_conf

        except Exception as e:
            print(f"[PersonDetector] 오류 발생: {str(e)}")
            return 0.0
