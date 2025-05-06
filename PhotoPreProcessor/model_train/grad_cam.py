import cv2
import numpy as np
import matplotlib.pyplot as plt
import torch.nn.functional as F
import torch
from efficientnet_model import EfficientNetClassifier
from torchvision import transforms
from PIL import Image
from utils import pad_to_square

def apply_gradcam(model, image_tensor, device, class_idx=None):
    model.eval()
    image_tensor = image_tensor.unsqueeze(0).to(device)

    # hook 저장용
    gradients = []
    activations = []

    def save_gradient_full(module, grad_input, grad_output):
        gradients.append(grad_output[0])

    def save_activation(module, input, output):
        activations.append(output)

    # 마지막 Conv 레이어 연결
    final_conv = model.model.features[-1]
    final_conv.register_forward_hook(save_activation)
    final_conv.register_full_backward_hook(save_gradient_full)

    output = model(image_tensor)
    pred_class = output.argmax(dim=1).item() if class_idx is None else class_idx
    score = output[:, pred_class]

    # 역전파
    model.zero_grad()
    score.backward()

    # Grad-CAM 계산
    grad = gradients[0].cpu().data.numpy()[0]
    activation = activations[0].cpu().data.numpy()[0]
    weights = np.mean(grad, axis=(1, 2))
    cam = np.zeros(activation.shape[1:], dtype=np.float32)

    for i, w in enumerate(weights):
        cam += w * activation[i]
    cam = np.maximum(cam, 0)
    cam = cv2.resize(cam, (224, 224))
    cam -= cam.min()
    cam /= cam.max()
    return cam

# 시각화 함수
def show_gradcam(image_pil, cam):
    img = np.array(image_pil.resize((224, 224)))
    heatmap = (cam * 255).astype(np.uint8)
    heatmap = cv2.applyColorMap(heatmap, cv2.COLORMAP_JET)
    overlay = cv2.addWeighted(img, 0.5, heatmap, 0.5, 0)

    plt.figure(figsize=(8, 4))
    plt.subplot(1, 2, 1)
    plt.imshow(img)
    plt.title("Original Image")
    plt.subplot(1, 2, 2)
    plt.imshow(overlay)
    plt.title("Grad-CAM")
    plt.show()
# 이미지 준비

# 실행 함수 
def exec(image_path):
    image = Image.open(image_path).convert("RGB")
    transform = transforms.Compose([
        transforms.Lambda(pad_to_square),
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406],
                            [0.229, 0.224, 0.225])
    ])
    tensor = transform(image)

    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print("Using device:", device)
    dummy_loader = []  # 추론에선 DataLoader 필요 없음
    model = EfficientNetClassifier(dummy_loader, dummy_loader, device)
    model.load_model("./model_weight/efficientnet_real_fake_v1.pth")
    # Grad-CAM 실행
    cam = apply_gradcam(model, tensor, device)
    show_gradcam(image, cam)

exec("./sample/ai_generated/fake13.jpg")