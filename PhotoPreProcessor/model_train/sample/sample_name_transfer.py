import os
from PIL import Image

# 원본 폴더 경로 설정
input_folder = "./new_sample"
start_index = 30

# 폴더 내 파일 리스트를 정렬된 순서로 가져오기
file_list = sorted([f for f in os.listdir(input_folder) if f.lower().endswith(".tif")])

for i, filename in enumerate(file_list):
    old_path = os.path.join(input_folder, filename)
    
    # 새로운 파일명 지정
    new_filename = f"fake{start_index + i}.jpg"
    new_path = os.path.join(input_folder, new_filename)

    # tif 파일을 열어서 jpg로 변환 및 저장
    with Image.open(old_path) as img:
        rgb_img = img.convert("RGB")  # JPEG은 RGB 포맷만 지원
        rgb_img.save(new_path, "JPEG")

    print(f"변환 완료: {filename} → {new_filename}")
