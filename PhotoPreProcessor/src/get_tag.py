import torch
import torchvision.models as models
from torchvision import transforms
from PIL import Image
import os
import urllib.request

def download_places365_model():
  url = 'http://places2.csail.mit.edu/models_places365/resnet50_places365.pth.tar'
  model_path = './resnet50_places365.pth.tar'

  if not os.path.exists(model_path):
    print(f"downloding places365")
    try:
      urllib.request.urlretrieve(url, model_path)
      print(f"places365 successful download")
    except Exception as e:
      print(f"places365 download error")
      return False
  return True

def predict(image_path):
  resnet_imagenet = models.resnet50(weights='IMAGENET1K_V2')
  resnet_imagenet.eval()

  resnet_places = models.resnet50()
  resnet_places.fc = torch.nn.Linear(resnet_places.fc.in_features, 365)


  places_state = torch.load('./resnet50_places365.pth.tar', weights_only=True, map_location=torch.device('cpu'))['state_dict']
  places_state = {k.replace('module.', ''): v for k, v in places_state.items()}
  resnet_places.load_state_dict(places_state)
  resnet_places.eval()

  transform = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
  ])

  image = Image.open(image_path).convert('RGB')
  input_tensor = transform(image).unsqueeze(0)

  device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
  resnet_imagenet = resnet_imagenet.to(device)
  resnet_places = resnet_places.to(device)
  input_tensor = input_tensor.to(device)

  with torch.no_grad():
    imagenet_output = resnet_imagenet(input_tensor)
    imagenet_probs = torch.softmax(imagenet_output, dim=1)[0]

  with torch.no_grad():
    places_output = resnet_places(input_tensor)
    places_probs = torch.softmax(places_output, dim=1)[0]

  imagenet_results = []
  for category, indices in imagenet_categories.items():
    category_prob = sum(imagenet_probs[idx].item() for idx in indices)
    if category_prob > 0.1:
        imagenet_results.append({
          'category': category,
          'probability': category_prob
        })

  places_results = []
  for category, indices in places_categories.items():
    category_prob = sum(places_probs[idx].item() for idx in indices)
    if category_prob > 0.1:
      places_results.append({
        'category': category,
        'probability': category_prob
        })

  return {
    'animals': sorted(imagenet_results, key=lambda x: x['probability'], reverse=True),
    'places': sorted(places_results, key=lambda x: x['probability'], reverse=True)
  }

# https://deeplearning.cms.waikato.ac.nz/user-guide/class-maps/IMAGENET/ 카테고리 내용 
# 개, 고양이, 다람쥐, 햄스터, 새, 곤충, 파충류, 해양생물, 물고기, 돼지
imagenet_categories = {
    '개': list(range(151, 269)),
    '고양이': [281, 282, 283, 284, 285],
    '다람쥐': [335],
    '햄스터': [333],
    '새': [7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146],
    '곤충': list(range(300, 327)),
    '파충류': [33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68],
    '해양생물': [107, 108, 109, 118, 119, 120, 121, 122, 123, 147, 148, 149, 150],
    '물고기': [0, 1, 2, 3, 4, 5, 6, 389, 390, 391, 393, 394, 395, 396, 397],
    '돼지' : [338, 341]
}


# http://places2.csail.mit.edu/models_places365/categories_places365.txt 카테고리 내용
# 산, 바다, 강, 들판, 숲, 하늘
places_categories = {
    '산': [232, 233, 234],
    '바다': [243, 342, 357, 48, 97],
    '강': [205, 271, 145],
    '들판': [140, 141, 142, 138, 104, 359, 287, 258, 209],
    '숲': [150, 151, 152, 36, 279],
    '하늘': [306],
}

def tagging(image_path) :
  download_places365_model()
  results = predict(image_path)

  animal_category = 'other'
  animal_probability = 0
  place_category = 'other'
  place_probability = 0

  if results['animals']:
    for pred in results['animals']:
      animal_category = pred['category']
      animal_probability = pred['probability']

  if results['places']:
    for pred in results['places']:
      place_category = pred['category']
      place_probability = pred['probability']

  if place_probability > animal_probability:
    return place_category
  else :
    return animal_category