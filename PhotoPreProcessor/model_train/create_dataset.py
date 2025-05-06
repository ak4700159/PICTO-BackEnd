import kagglehub
import os
from dataset.real_fake_dataset import RealFakeDataset
from dataset.real_fake_folder_dataset import RealFakeFolderDataset
from datasets import load_dataset
from torchvision import datasets
import torch
from torch.utils.data import DataLoader, random_split, ConcatDataset

from dataset.ela_dataset import ELADataset  # ELADataset 클래스 정의한 파일에서 import
from glob import glob
from random import sample
from sklearn.model_selection import train_test_split

# AI 생성 사진 분류 모델 학습 데이터
# 데이터셋 출처 : https://www.kaggle.com/datasets/duyminhle/real-fake-image-full-dataset/data?select=1_fake
# path = kagglehub.dataset_download("duyminhle/real-fake-image-full-dataset")
# print("Path to dataset files:", path)
# Path to dataset files: C:\Users\cn120\.cache\kagglehub\datasets\duyminhle\real-fake-image-full-dataset\versions\1

# AI 생성 사진 분류 모델 학습 데이터
# 데이터셋 출처 : https://www.kaggle.com/datasets/tristanzhang32/ai-generated-images-vs-real-images/data
# path = kagglehub.dataset_download("tristanzhang32/ai-generated-images-vs-real-images")
# print("Path to dataset files:", path)
# Path to dataset files: C:\Users\cn120\.cache\kagglehub\datasets\tristanzhang32\ai-generated-images-vs-real-images\versions\2

# 이 외에도 허깅페이스에서도 다운로드했음.

# 합성 사진 분류 모델 학습 데이터 
# 데이터셋 출처 : https://www.kaggle.com/datasets/divg07/casia-20-image-tampering-detection-dataset/data
path = kagglehub.dataset_download("divg07/casia-20-image-tampering-detection-dataset")
print("Path to dataset files:", path)
# Path to dataset files: C:\Users\cn120\.cache\kagglehub\datasets\divg07\casia-20-image-tampering-detection-dataset\versions\1

def train_real_fake_single_dataset(transform):
    # kagglehub 경로 예시
    folder_path = r"C:\Users\cn120\.cache\kagglehub\datasets\tristanzhang32\ai-generated-images-vs-real-images\versions\2"
    folder_dataset = RealFakeFolderDataset(folder_path, transform=transform)
    train_dir = os.path.join(folder_path, "train")
    test_dir = os.path.join(folder_path, "test")

    # PyTorch ImageFolder로 불러오기
    train_dataset = datasets.ImageFolder(train_dir, transform=transform)
    val_dataset = datasets.ImageFolder(test_dir, transform=transform)

    train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)
    val_loader = DataLoader(val_dataset, batch_size=32, shuffle=False)

    # ✅ 확인
    print(f"로컬 이미지 수: {len(folder_dataset)}")
    print(f"전체 학습용 샘플 수: {len(train_dataset)}")
    print(f"전체 검증용 샘플 수: {len(val_dataset)}")
    return train_loader, val_loader

def train_combined_dataset(transform): 
    folder_path = r"C:\Users\cn120\.cache\kagglehub\datasets\duyminhle\real-fake-image-full-dataset\versions\1"

    # 📦 Dataset 생성
    folder_dataset = RealFakeFolderDataset(folder_path, transform=transform)
    hf_dataset_raw = load_dataset("date3k2/raw_real_fake_images")
    hf_dataset = RealFakeDataset(hf_dataset_raw["train"], transform=transform)

    # 🔀 병합 + 8:2 비율 분리
    full_combined_dataset = ConcatDataset([folder_dataset, hf_dataset])
    train_size = int(0.8 * len(full_combined_dataset))
    val_size = len(full_combined_dataset) - train_size
    generator = torch.Generator().manual_seed(42)

    train_dataset, val_dataset = random_split(full_combined_dataset, [train_size, val_size], generator=generator)

    # 📤 DataLoader
    train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)
    val_loader = DataLoader(val_dataset, batch_size=32, shuffle=False)

    # ✅ 확인
    print(f"로컬 이미지 수: {len(folder_dataset)}")
    print(f"HuggingFace 이미지 수: {len(hf_dataset)}")
    print(f"전체 학습용 샘플 수: {len(train_dataset)}")
    print(f"전체 검증용 샘플 수: {len(val_dataset)}")
    return train_loader, val_loader

def train_tampering_single_dataset(transform, batch_size=32, num_workers=1):
    # 데이터셋 기본 경로 설정 (사용자 환경에 맞게 수정 가능)
    base_path = r"C:\Users\cn120\.cache\kagglehub\datasets\divg07\casia-20-image-tampering-detection-dataset\versions\1\CASIA2"
    au_dir = os.path.join(base_path, "Au")
    tp_dir = os.path.join(base_path, "Tp")

    # ✅ 확장자별 수집
    def collect_images(folder):
        return glob(os.path.join(folder, "*.jpg")) + \
               glob(os.path.join(folder, "*.png")) + \
               glob(os.path.join(folder, "*.tif"))

    real_paths = collect_images(au_dir)
    fake_paths = collect_images(tp_dir)

    # ✅ 언더샘플링: real 이미지를 fake 수에 맞춤
    selected_real = sample(real_paths, len(fake_paths))

    all_paths = selected_real + fake_paths
    all_labels = [1] * len(selected_real) + [0] * len(fake_paths)

    # 학습/검증 분할
    X_train, X_val, y_train, y_val = train_test_split(all_paths, all_labels, test_size=0.2, random_state=42)

    # 커스텀 Dataset 구성
    train_dataset = ELADataset(X_train, y_train, transform=transform)
    val_dataset = ELADataset(X_val, y_val, transform=transform)

    # DataLoader 구성
    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True, num_workers=num_workers)
    val_loader = DataLoader(val_dataset, batch_size=batch_size, shuffle=False, num_workers=num_workers)

    print(f"로컬 이미지 수: {len(all_paths)}")
    print(f"전체 학습용 샘플 수: {len(train_dataset)}")
    print(f"전체 검증용 샘플 수: {len(val_dataset)}")
    return train_loader, val_loader