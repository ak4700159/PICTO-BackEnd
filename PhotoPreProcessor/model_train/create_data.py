import kagglehub
import os
from dataset.real_fake_dataset import RealFakeDataset
from dataset.real_fake_folder_dataset import RealFakeFolderDataset
from datasets import load_dataset
from torchvision import datasets
import torch
from torch.utils.data import DataLoader, random_split, ConcatDataset


# 데이터셋 출처 : https://www.kaggle.com/datasets/duyminhle/real-fake-image-full-dataset/data?select=1_fake
# path = kagglehub.dataset_download("duyminhle/real-fake-image-full-dataset")

# print("Path to dataset files:", path)
# Path to dataset files: C:\Users\cn120\.cache\kagglehub\datasets\duyminhle\real-fake-image-full-dataset\versions\1


# 데이터셋 출처 : https://www.kaggle.com/datasets/tristanzhang32/ai-generated-images-vs-real-images/data
path = kagglehub.dataset_download("tristanzhang32/ai-generated-images-vs-real-images")

print("Path to dataset files:", path)
# Path to dataset files: C:\Users\cn120\.cache\kagglehub\datasets\tristanzhang32\ai-generated-images-vs-real-images\versions\2


def train_single_dataset(transform):
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