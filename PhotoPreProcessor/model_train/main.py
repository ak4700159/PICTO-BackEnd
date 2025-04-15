from torchvision import transforms
from efficientnet_model import EfficientNetClassifier
from utils import pad_to_square
import torch
from create_data import train_single_dataset
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True


if __name__ == '__main__':
        # GPU 확인
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print("Using device:", device)

    # 전처리 정의 (사이즈 통일 + 텐서 변환)
    transform = transforms.Compose([
        transforms.Lambda(pad_to_square),     # ⬅️ 종횡비 유지하면서 정사각형 패딩
        transforms.Resize((224, 224)),        # 모델 입력 크기로 조정
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406],  # EfficientNet 기준
                            std=[0.229, 0.224, 0.225])
    ])

    # 📤 DataLoader
    train_loader,val_loader = train_single_dataset(transform=transform)

    # model = SimpleCNN(train_loader, val_loader, device)
    # model.train_model(num_epochs=10)
    model = EfficientNetClassifier(train_loader, val_loader, device)
    model.train_model(num_epochs=20)





    