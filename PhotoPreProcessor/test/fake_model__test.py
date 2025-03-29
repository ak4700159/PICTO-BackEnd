# 2. Vision Transformer (ViT) Fine-Tuned 모델
# 용도: 일반 합성 이미지 감지 (사람, 풍경, 동물 등 전반적)
from transformers import AutoImageProcessor, AutoModelForImageClassification
from PIL import Image
import torch

from datasets import load_dataset

ds = load_dataset("date3k2/raw_real_fake_images")




# model = AutoModelForImageClassification.from_pretrained("aliaksandrsiarohin/fake-image-detector")
# processor = AutoImageProcessor.from_pretrained("aliaksandrsiarohin/fake-image-detector")

# img = Image.open("합성사진.jpg").convert("RGB")
# inputs = extractor(images=img, return_tensors="pt")
# outputs = model(**inputs)
# pred = torch.argmax(outputs.logits, dim=1).item()

# print("Fake" if pred == 1 else "Real")