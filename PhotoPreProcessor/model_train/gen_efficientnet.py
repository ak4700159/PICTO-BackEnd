from torchvision import models, transforms
from torch import nn, optim
import torch
from PIL import Image
from io import BytesIO
import os
import time
from datetime import datetime
from model_train.utils import pad_to_square

class GenEfficientNetClassifier(nn.Module):
    def __init__(self, train_loader, val_loader, device):
        super(GenEfficientNetClassifier, self).__init__()
        self.device = device
        self.train_loader = train_loader
        self.val_loader = val_loader

        # ✅ EfficientNet B2로 변경
        self.model = models.efficientnet_b2(weights=models.EfficientNet_B2_Weights.DEFAULT)
        in_features = self.model.classifier[1].in_features
        self.model.classifier[1] = nn.Linear(in_features, 2)

        self.to(self.device)
        self.criterion = nn.CrossEntropyLoss()
        self.optimizer = optim.Adam(self.parameters(), lr=1e-4)

        # ✅ ReduceLROnPlateau 스케줄러 추가
        self.scheduler = optim.lr_scheduler.ReduceLROnPlateau(self.optimizer, mode='min', factor=0.5, patience=2)

    def forward(self, x):
        return self.model(x)

    def train_model(self, num_epochs=5):
        best_loss = float('inf')  # 🔥 전역 최적 loss 저장
        for epoch in range(num_epochs):
            # ✅ 현재 시간 출력
            now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            print(f"\n🕒 Epoch {epoch + 1} 시작 - 현재 시간: {now}")
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
            val_loss = self.validate()
            self.scheduler.step(val_loss)

    def save_model(self, path="gen_v3.pth"):
        torch.save(self.state_dict(), path)
        print(f"✅ Model saved to: {path}")

    def load_model(self, path, map_location=None):
        if not os.path.exists(path):
            raise FileNotFoundError(f"❌ Model file '{path}' not found.")

        state_dict = torch.load(path, map_location=map_location or self.device, weights_only = True)
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
        avg_val_loss = val_loss / len(self.val_loader)
        print(f"Validation Accuracy: {acc:.4f}, Loss: {avg_val_loss:.4f}")
        return avg_val_loss


    def predict_image(self, image_path):
        self.eval()
        image = Image.open(image_path)
        if image.mode != "RGB":
            image = image.convert("RGB")

        transform_train = transforms.Compose([
            transforms.Lambda(pad_to_square),
            transforms.Resize((224, 224)),
            transforms.RandomHorizontalFlip(),
            transforms.RandomRotation(10),
            transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2),
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.485, 0.456, 0.406],
                                std=[0.229, 0.224, 0.225])
        ])

        image_tensor = transform_train(image).unsqueeze(0).to(self.device)
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

            transform_train = transforms.Compose([
                transforms.Lambda(pad_to_square),
                transforms.Resize((224, 224)),
                transforms.RandomHorizontalFlip(),
                transforms.RandomRotation(10),
                transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2),
                transforms.ToTensor(),
                transforms.Normalize(mean=[0.485, 0.456, 0.406],
                                    std=[0.229, 0.224, 0.225])
            ])

            image_tensor = transform_train(image).unsqueeze(0).to(self.device)
            with torch.no_grad():
                output = self(image_tensor)
                pred = torch.argmax(output, dim=1).item()

            return "Real" if pred == 1 else "Fake"

        except Exception as e:
            print(f"❌ 이미지 처리 중 오류 발생: {e}")
            return None