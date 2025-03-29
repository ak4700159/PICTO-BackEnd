from torch import nn, optim
import torch
from PIL import Image
from io import BytesIO
import torchvision.transforms as transforms
import os
import time

class SimpleCNN(nn.Module):
    def __init__(self, train_loader, val_loader, device):
        super(SimpleCNN, self).__init__()
        self.device = device
        self.train_loader = train_loader
        self.val_loader = val_loader

        self.net = nn.Sequential(
            # 컨볼루션 계충
            nn.Conv2d(3, 32, 3, padding=1), nn.BatchNorm2d(32), nn.ReLU(), nn.MaxPool2d(2),
            nn.Conv2d(32, 64, 3, padding=1), nn.BatchNorm2d(64), nn.ReLU(), nn.MaxPool2d(2),
            nn.AdaptiveAvgPool2d((7, 7)),
            nn.Flatten(),
            # 완전 연결 계층
            nn.Linear(64 * 7 * 7, 128), nn.BatchNorm1d(128), nn.ReLU(),
            # 최종적으로 2개의 라벨로 분류 -> Real(1) OR Fake(0)
            nn.Linear(128, 2)
        )

        self.to(self.device)
        self.criterion = nn.CrossEntropyLoss()
        self.optimizer = optim.Adam(self.parameters(), lr=1e-4)

    def forward(self, x):
        return self.net(x)

    def train_model(self, num_epochs=5):
        for epoch in range(num_epochs):
            start_time = time.time()
            self.train()
            total_loss = 0

            for images, labels in self.train_loader:
                images, labels = images.to(self.device), labels.to(self.device)

                outputs = self(images)
                loss = self.criterion(outputs, labels)

                self.optimizer.zero_grad()
                loss.backward()
                self.optimizer.step()

                total_loss += loss.item()

            elapsed_time = time.time() - start_time
            minutes, seconds = divmod(elapsed_time, 60)  # 분:초 나누기

            print(f"Epoch {epoch + 1}, Loss: {total_loss:.4f}, Time: {int(minutes)}m {int(seconds)}s")
            self.validate()

    def save_model(self, path="real_fake_model.pth"):
        torch.save(self.state_dict(), path)
        print(f"✅ Model saved to: {path}")

    def load_model(self, path, map_location=None):
        if not os.path.exists(path):
            raise FileNotFoundError(f"❌ Model file '{path}' not found.")

        state_dict = torch.load(path, map_location=map_location or self.device)
        self.load_state_dict(state_dict)
        self.to(self.device)
        print(f"📦 Model loaded from: {path}")

    def validate(self):
        self.eval()
        correct, total = 0, 0
        val_loss = 0.0
        with torch.no_grad():
            for images, labels in self.val_loader:
                images, labels = images.to(self.device), labels.to(self.device)
                outputs = self(images)
                loss = self.criterion(outputs, labels)
                val_loss += loss.item()

                preds = torch.argmax(outputs, dim=1)
                correct += (preds == labels).sum().item()
                total += labels.size(0)

        acc = correct / total
        print(f"Validation Accuracy: {acc:.4f}, Loss: {val_loss:.4f}")

# 저장된 이미지 파일 불러와 예측
    def predict_image(self, image_path):
        self.eval()  # 추론 모드 전환

        # 이미지 불러오기
        image = Image.open(image_path)

        # RGB로 변환 (투명도 제거 등)
        if image.mode != "RGB":
            image = image.convert("RGB")

        # transform 정의 (모델 학습 시 사용한 것과 동일해야 함)
        transform = transforms.Compose([
            transforms.Resize((224, 224)),
            transforms.ToTensor()
        ])

        image_tensor = transform(image).unsqueeze(0).to(self.device)  # 배치 차원 추가

        with torch.no_grad():
            output = self(image_tensor)
            pred = torch.argmax(output, dim=1).item()

        return "Real" if pred == 1 else "Fake"

# 바이트단위로 전송 받은 이미지 바로 처리
    def predict_image_from_bytes(self, image_bytes):
        self.eval()

        try:
            image = Image.open(BytesIO(image_bytes))

            if image.mode != "RGB":
                image = image.convert("RGB")

            transform = transforms.Compose([
                transforms.Resize((224, 224)),
                transforms.ToTensor()
            ])

            image_tensor = transform(image).unsqueeze(0).to(self.device)

            with torch.no_grad():
                output = self(image_tensor)
                pred = torch.argmax(output, dim=1).item()

            return "Real" if pred == 1 else "Fake"

        except Exception as e:
            print(f"❌ 이미지 처리 중 오류 발생: {e}")
            return None