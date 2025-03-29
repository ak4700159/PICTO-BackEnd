import torch
from PIL import Image

Image.MAX_IMAGE_PIXELS = None

class RealFakeDataset(torch.utils.data.Dataset):
    def __init__(self, hf_dataset, transform=None):
        self.dataset = hf_dataset
        self.transform = transform

    def __len__(self):
        return len(self.dataset)

    # def __getitem__(self, idx):
    #     image = self.dataset[idx]["image"]
    #     label = self.dataset[idx]["label"]
    #     if self.transform:
    #         image = self.transform(image)
    #     return image, torch.tensor(label, dtype=torch.long)

    def __getitem__(self, idx):
        try:
            image = self.dataset[idx]["image"]
            label = self.dataset[idx]["label"]

            if image.mode != "RGB":
                image = image.convert("RGB")

            if self.transform:
                image = self.transform(image)

            return image, torch.tensor(label, dtype=torch.long)

        except (Image.DecompressionBombError, UnidentifiedImageError) as e:
            print(f"⚠ Skipping problematic image at index {idx}: {e}")
            return self.__getitem__((idx + 1) % len(self))