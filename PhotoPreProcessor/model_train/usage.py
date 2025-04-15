import os
import torch
from efficientnet_model import EfficientNetClassifier

from PIL import Image

def detect_from_folder(folder_path="./sample"):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print("Using device:", device)

    dummy_loader = []  # 추론에선 DataLoader 필요 없음
    model = EfficientNetClassifier(dummy_loader, dummy_loader, device)
    model.load_model("efficientnet_real_fake.pth")

    image_files = [f for f in os.listdir(folder_path) if f.lower().endswith((".jpg", ".jpeg", ".png"))]

    total = 0
    correct = 0

    for fname in image_files:
        path = os.path.join(folder_path, fname)
        result = model.predict_image(path)

        # 이름 기반 정답 추정: 'real' or 'fake' 포함 여부 (대소문자 무시)
        lower_name = fname.lower()
        if "real" in lower_name:
            label = "Real"
        elif "fake" in lower_name or "합성" in lower_name:
            label = "Fake"
        else:
            print(f"⚠️ 파일명에 라벨 키워드 없음: {fname}")
            continue  # skip unknown

        is_correct = (result == label)
        print(f"🖼️ {fname} → 예측: {result}, 정답: {label} ✅" if is_correct else f"🖼️ {fname} → 예측: {result}, 정답: {label} ❌")

        total += 1
        correct += int(is_correct)

    if total == 0:
        print("❌ 평가할 수 있는 이미지가 없습니다.")
        return

    accuracy = correct / total * 100
    print(f"\n📊 전체 정확도: {correct} / {total} ({accuracy:.2f}%)")

if __name__ == "__main__":
    detect_from_folder("./sample")
