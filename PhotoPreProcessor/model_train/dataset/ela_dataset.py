from torch.utils.data import Dataset
import torch
from PIL import Image, ImageChops, ImageEnhance
import numpy as np
import os

def convert_to_ela_image(path, quality=90):
    try:
        temp_filename = 'temp_ela.jpg'

        image = Image.open(path).convert('RGB')
        image = image.copy()  # 💡 TIFF 문제 회피: Lazy decoding → 명시적 복사
        image.save(temp_filename, 'JPEG', quality=quality)
        temp_image = Image.open(temp_filename)

        ela_image = ImageChops.difference(image, temp_image)
        extrema = ela_image.getextrema()
        max_diff = max([ex[1] for ex in extrema])
        if max_diff == 0:
            max_diff = 1
        scale = 255.0 / max_diff
        ela_image = ImageEnhance.Brightness(ela_image).enhance(scale)

        return ela_image.resize((128, 128))

    except Exception as e:
        print(f"❌ ELA 변환 실패: {path} / {e}")
        return Image.new("RGB", (128, 128), (0, 0, 0))  # 실패 시 검은 이미지 반환

class ELADataset(Dataset):
    def __init__(self, image_paths, labels, transform=None):
        self.image_paths = image_paths
        self.labels = labels
        self.transform = transform
        
    def __len__(self):
        return len(self.image_paths)

    def __getitem__(self, idx):
        image = convert_to_ela_image(self.image_paths[idx])
        if self.transform:
            image = self.transform(image)
        label = self.labels[idx]
        return image, label
