import torch
from torchvision import transforms
from PIL import Image
from model_train.dataset.ela_dataset import convert_to_ela_image
from model_train.utils import pad_to_square
from model_train.gen_efficientnet import GenEfficientNetClassifier 


class GenDetector:
    def __init__(self, model_path="../model_train/model_weight/gen_v3.pth", threshold=0.5):
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.threshold = threshold

        # 모델 로드
        self.model = GenEfficientNetClassifier(train_loader=None, val_loader=None, device=self.device)
        self.model.load_model(model_path)
        self.model.eval()

        # 전처리 정의
        self.transform = transforms.Compose([
            transforms.Lambda(pad_to_square),
            transforms.Resize((224, 224)),
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.485, 0.456, 0.406],
                                 std=[0.229, 0.224, 0.225])
        ])

    def detect(self, image_path):
        try:
            image = Image.open(image_path).convert("RGB")
            image_tensor = self.transform(image).unsqueeze(0).to(self.device)

            with torch.no_grad():
                output = self.model(image_tensor)
                prob = torch.softmax(output, dim=1)[0][0].item()  # 0: Generated, 1: Real

            return prob

        except Exception as e:
            print(f"[GenDetector] 이미지 처리 오류: {str(e)}")
            return 0.0