import torch
from torchvision import models, transforms
from PIL import Image
import torch.nn as nn

class PersonDetectionModel(nn.Module):
  def __init__(self):
    super(PersonDetectionModel, self).__init__()
    self.model = models.resnet18(weights=models.ResNet18_Weights.DEFAULT)

    num_ftrs = self.model.fc.in_features
    self.model.fc = nn.Sequential(
        nn.Dropout(0.5),
        nn.Linear(num_ftrs, 1),
        nn.Sigmoid()
    )
  
  def forward(self, x):
    return self.model(x)

# 사람 감지를 위한 모델(미리 저장된 모델의 가중치를 가지고 온다. ./person_detection.pth)
class PersonDetector:
  def __init__(self, model_path):
    self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    self.model = PersonDetectionModel()

    checkpoint = torch.load(model_path, map_location=self.device)
    self.model.load_state_dict(checkpoint['model_state_dict'])
    self.model.to(self.device)
    self.model.eval()

    self.transform = transforms.Compose([
      transforms.Resize((224, 224)),
      transforms.ToTensor(),
      transforms.Normalize(
        mean=[0.485, 0.456, 0.406],
        std=[0.229, 0.224, 0.225]
        )
      ])

  def detect(self, image_path):
    image = Image.open(image_path).convert('RGB')
    image_tensor = self.transform(image).unsqueeze(0).to(self.device)

    with torch.no_grad():
      output = self.model(image_tensor)
      probability = output.item()

    return probability > 0.5 #, probability
  

# 테스트 코드
# def main():
#   detector = PersonDetector('./person_detection.pth')

#   image_path = './test/persons.jpeg'
#   is_person, confidence = detector.detect(image_path)

#   return is_person
# #   print(f"사람 감지 결과: {is_person}")
# #   print(f"확률: {confidence:.2%}")

# if __name__ == '__main__':
#   main()