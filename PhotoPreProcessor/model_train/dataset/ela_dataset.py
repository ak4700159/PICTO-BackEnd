from torch.utils.data import Dataset
import torch
from PIL import Image, ImageChops, ImageEnhance
import numpy as np
import os

# 실패 이미지 경로 저장용
FAILED_ELA_PATHS = []

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
        FAILED_ELA_PATHS.append(path)
        return None
        # return Image.new("RGB", (128, 128), (0, 0, 0))  # 실패 시 검은 이미지 반환

class ELADataset(Dataset):
    def __init__(self, image_paths, labels, transform=None):
        self.data = []
        self.transform = transform

        for path, label in zip(image_paths, labels):
            ela_img = convert_to_ela_image(path)
            if ela_img is not None:
                self.data.append((ela_img, label))

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        image, label = self.data[idx]
        if self.transform:
            image = self.transform(image)
        return image, label