import torch
from torchvision import transforms
from PIL import Image
# from model_train.dataset.ela_dataset import convert_to_ela_image
# from model_train.utils import pad_to_square
# from model_train.tampering_efficientnet import TamperingEfficientNetClassifier  # 학습한 모델 클래스가 별도 파일에 있다면 이처럼 import
from model_train.dataset.ela_dataset import convert_to_ela_image
from model_train.utils import pad_to_square
from model_train.tampering_efficientnet import TamperingEfficientNetClassifier  # 학습한 모델 클래스가 별도 파일에 있다면 이처럼 import

class TamperingDetector:
    def __init__(self, model_path="../model_train/model_weight/tampering_v2.pth", threshold=0.5):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.threshold = threshold

        # 더미 데이터로 초기화 후 학습된 가중치 불러오기
        self.model = TamperingEfficientNetClassifier(train_loader=None, val_loader=None, device=self.device)
        self.model.load_model(model_path)
        self.model.eval()

        # 전처리 함수 정의
        self.transform = transforms.Compose([
            transforms.Lambda(pad_to_square),
            transforms.Resize((224, 224)),
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.485, 0.456, 0.406],
                                 std=[0.229, 0.224, 0.225])
        ])

    def detect(self, image_path):
        try:
            # ELA 변환 적용
            ela_image = convert_to_ela_image(image_path)
            if ela_image is None:
                print(f"[TamperingDetector] ELA 변환 실패: {image_path}")
                return 0.0  # 실패 시는 검출 확률 0으로 처리

            # 이미지 변환 및 모델 예측
            image_tensor = self.transform(ela_image).unsqueeze(0).to(self.device)

            with torch.no_grad():
                output = self.model(image_tensor)
                prob = torch.softmax(output, dim=1)[0][0].item()  # 클래스 0: Fake (Tampered), 1: Real

            return prob  # 0~1 사이 확률 반환

        except Exception as e:
            print(f"[TamperingDetector] 이미지 처리 오류: {str(e)}")
            return 0.0
