from torchvision import models, transforms
from torch import nn, optim
import torch
from PIL import Image
from io import BytesIO
import os
import time
from utils import pad_to_square

class EfficientNetClassifier(nn.Module):
    def __init__(self, train_loader, val_loader, device):
        super(EfficientNetClassifier, self).__init__()
        self.device = device
        self.train_loader = train_loader
        self.val_loader = val_loader

        # Load pretrained EfficientNet
        self.model = models.efficientnet_b0(weights=models.EfficientNet_B0_Weights.DEFAULT)

        # Replace the classifier head for binary classification
        in_features = self.model.classifier[1].in_features
        self.model.classifier[1] = nn.Linear(in_features, 2)

        self.to(self.device)
        self.criterion = nn.CrossEntropyLoss()
        self.optimizer = optim.Adam(self.parameters(), lr=1e-4)

    def forward(self, x):
        return self.model(x)

    def train_model(self, num_epochs=5):
        best_loss = float('inf')  # 🔥 전역 최적 loss 저장

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
            minutes, seconds = divmod(elapsed_time, 60)
            avg_loss = total_loss / len(self.train_loader)

            # ✅ 최적 성능일 때만 저장
            if avg_loss < best_loss:
                best_loss = avg_loss
                self.save_model()
                print(f"🧠 Best model updated (Loss: {avg_loss:.4f})")

            print(f"Epoch {epoch + 1}, Loss: {avg_loss:.4f}, Time: {int(minutes)}m {int(seconds)}s")
            self.validate()
            
    def save_model(self, path="efficientnet_real_fake.pth"):
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
        print(f"Validation Accuracy: {acc:.4f}, Loss: {val_loss / len(self.val_loader):.4f}")

    def predict_image(self, image_path):
        self.eval()
        image = Image.open(image_path)
        if image.mode != "RGB":
            image = image.convert("RGB")

        transform = transforms.Compose([
            transforms.Lambda(pad_to_square),     # ⬅️ 종횡비 유지하면서 정사각형 패딩
            transforms.Resize((224, 224)),        # 모델 입력 크기로 조정
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.485, 0.456, 0.406],  # EfficientNet 기준
                                std=[0.229, 0.224, 0.225])
        ])

        image_tensor = transform(image).unsqueeze(0).to(self.device)
        with torch.no_grad():
            output = self(image_tensor)
            pred = torch.argmax(output, dim=1).item()

        return "Real" if pred == 1 else "Fake"

    def predict_image_from_bytes(self, image_bytes):
        self.eval()
        try:
            image = Image.open(BytesIO(image_bytes))
            if image.mode != "RGB":
                image = image.convert("RGB")

            transform = transforms.Compose([
                transforms.Lambda(pad_to_square),     # ⬅️ 종횡비 유지하면서 정사각형 패딩
                transforms.Resize((224, 224)),        # 모델 입력 크기로 조정
                transforms.ToTensor(),
                transforms.Normalize(mean=[0.485, 0.456, 0.406],  # EfficientNet 기준
                                    std=[0.229, 0.224, 0.225])
            ])

            image_tensor = transform(image).unsqueeze(0).to(self.device)
            with torch.no_grad():
                output = self(image_tensor)
                pred = torch.argmax(output, dim=1).item()

            return "Real" if pred == 1 else "Fake"

        except Exception as e:
            print(f"❌ 이미지 처리 중 오류 발생: {e}")
            return None
