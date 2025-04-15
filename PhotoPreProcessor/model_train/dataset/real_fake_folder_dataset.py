from torchvision import datasets
import torch
from PIL import Image

# 커스텀 Dataset 클래스와 호환되도록 이미지와 라벨을 추출
class RealFakeFolderDataset(torch.utils.data.Dataset):
    def __init__(self, root_dir, transform=None):
        self.dataset = datasets.ImageFolder(root=root_dir)
        self.transform = transform

    def __len__(self):
        return len(self.dataset)

    def __getitem__(self, idx):
        try:
            image, label = self.dataset[idx]
            if image.mode in ["P", "LA"]:
                image = image.convert("RGBA")  # 투명도 표현 포함
            if image.mode != "RGB":
                image = image.convert("RGB")   # 투명도 제거 및 RGB 통일
            if self.transform:
                image = self.transform(image)
            return image, torch.tensor(label, dtype=torch.long)

        except (Image.DecompressionBombError, Image.UnidentifiedImageError) as e:
            print(f"⚠ Skipping problematic image at index {idx}: {e}")
            return self.__getitem__((idx + 1) % len(self))
