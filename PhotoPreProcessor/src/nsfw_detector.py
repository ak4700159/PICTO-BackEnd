import torch
from transformers import AutoModelForImageClassification, AutoImageProcessor
from PIL import Image

# https://huggingface.co/Falconsai/nsfw_image_detection 허깅 페이스에서 가져온 모델
# 정확하게 어떤 사진들을 분류할 수 있는 확인해야됨
class NSFWDetector:
  def __init__(self, threshold=0.5):
    # GPU 환경 필요
    self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    model_name = "Falconsai/nsfw_image_detection"
    self.processor = AutoImageProcessor.from_pretrained(model_name)
    self.model = AutoModelForImageClassification.from_pretrained(model_name)
    self.model.to(self.device)
    self.model.eval()
    
    self.threshold = threshold

  def detect(self, image_path):
    try:
      image = Image.open(image_path).convert('RGB')
      inputs = self.processor(images=image, return_tensors="pt").to(self.device)

      with torch.no_grad():
          outputs = self.model(**inputs)
          probs = torch.softmax(outputs.logits, dim=1)

      nsfw_prob = probs[0][1].item()
      is_nsfw = nsfw_prob > self.threshold

      return is_nsfw #, nsfw_prob

    except Exception as e:
      print(f"이미지 처리 중 오류 발생: {str(e)}")
      return None #, None

# NSFW 테스트 용도 
# detector = NSFWDetector()

# def main():
#   image_path = './test/ro.jpeg' 

#   is_nsfw, probability = detector.detect(image_path)

#   if is_nsfw is not None:
#     return is_nsfw
#     #   print(f"NSFW 여부: {'Yes' if is_nsfw else 'No'}")
#     #   print(f"NSFW 확률: {probability:.2%}")

# if __name__ == '__main__':
#     main()