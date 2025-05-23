from torchvision import transforms
from gen_efficientnet import GenEfficientNetClassifier
from utils import pad_to_square
import torch
from create_dataset import train_real_fake_single_dataset
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True


# if __name__ == '__main__':
#         # GPU 확인
#     device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
#     print("Using device:", device)

#     # 전처리 정의 (사이즈 통일 + 텐서 변환)
#     transform_train = transforms.Compose([
#         transforms.Lambda(pad_to_square),
#         transforms.Resize((224, 224)),
#         transforms.RandomHorizontalFlip(),
#         transforms.RandomRotation(10),
#         transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2),
#         transforms.ToTensor(),
#         transforms.Normalize(mean=[0.485, 0.456, 0.406],
#                             std=[0.229, 0.224, 0.225])
#     ])
#     transform_val = transforms.Compose([
#         transforms.Lambda(pad_to_square),
#         transforms.Resize((224, 224)),
#         transforms.ToTensor(),
#         transforms.Normalize(mean=[0.485, 0.456, 0.406],
#                             std=[0.229, 0.224, 0.225])
#     ])
#     # 📤 DataLoader
#     train_loader,val_loader = train_tampering_single_dataset(train_transform=transform_train, val_transform=transform_val)
#     model = TamperingEfficientNetClassifier(train_loader, val_loader, device)
#     model.load_model("tampering.pth")
#     model.train_model(num_epochs=50)


if __name__ == '__main__':
        # GPU 확인
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print("Using device:", device)

    # 전처리 정의 (사이즈 통일 + 텐서 변환)
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

    # 📤 DataLoader
    train_loader,val_loader = train_real_fake_single_dataset(transform=transform_train)
    model = GenEfficientNetClassifier(train_loader, val_loader, device)
    # model.load_model("efficientnet_real_fake_v4.pth")
    model.train_model(num_epochs=20)