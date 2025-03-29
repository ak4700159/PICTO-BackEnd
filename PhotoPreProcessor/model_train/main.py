from torchvision import transforms
from torch.utils.data import DataLoader
from real_fake_dataset import RealFakeDataset
from real_fake_model import SimpleCNN
import torch
from datasets import load_dataset


if __name__ == '__main__':
        # GPU 확인
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print("Using device:", device)

    # 데이터셋 로딩
    dataset = load_dataset("date3k2/raw_real_fake_images")

    # 전처리 정의 (사이즈 통일 + 텐서 변환)
    # https://huggingface.co/datasets/date3k2/raw_real_fake_images/viewer
    transform = transforms.Compose([
        transforms.Resize((224, 224)),  # 고정 크기로 resize
        transforms.ToTensor(),          # [0, 1] 범위로 변환
    ])


    # DataLoader 구성
    train_dataset = RealFakeDataset(dataset["train"], transform=transform)
    val_dataset = RealFakeDataset(dataset["test"], transform=transform)

    train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)
    val_loader = DataLoader(val_dataset, batch_size=32)

    print(f"Training samples: {len(train_loader.dataset)}")
    print(f"Validation samples: {len(val_loader.dataset)}")
    
    model = SimpleCNN(train_loader, val_loader, device)
    model.train_model(num_epochs=10)