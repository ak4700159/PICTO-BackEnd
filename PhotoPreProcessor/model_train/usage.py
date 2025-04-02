import torch
from real_fake_model import SimpleCNN
from efficientnet_model import EfficientNetClassifier


def detect():
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print("Using device:", device)

    # 불필요한 train_loader 등은 None으로 대체 가능
    dummy_loader = []

    model = EfficientNetClassifier(dummy_loader, dummy_loader, device)
    model.load_model("efficientnet_real_fake.pth")

    image_path = "C:/capstone_workspace/PICTO-BackEnd/PhotoPreProcessor/model_train/sample/합성사진4.jpg"  # 예시 경로
    result = model.predict_image(image_path)
    print(f"🖼️ '{image_path}' 예측 결과: {result}")

detect()