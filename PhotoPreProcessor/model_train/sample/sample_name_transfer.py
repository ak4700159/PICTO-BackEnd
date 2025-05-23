import os
from PIL import Image

# 원본 폴더 경로
input_folder = "./tampering2"
start_index = 1

# 저장 대상 확장자
valid_ext = (".jpg", ".jpeg", ".png", ".tif", ".tiff")

# 정렬된 파일 목록 (유효 확장자만 포함)
file_list = sorted([f for f in os.listdir(input_folder) if f.lower().endswith(valid_ext)])

for i, filename in enumerate(file_list):
    old_path = os.path.join(input_folder, filename)

    # 새 파일명 생성
    new_filename = f"fake{start_index + i}.jpg"
    new_path = os.path.join(input_folder, new_filename)

    try:
        # 이미지 열고 RGB 변환 후 .jpg로 저장
        with Image.open(old_path) as img:
            rgb_img = img.convert("RGB")
            rgb_img.save("./tampering3", "JPEG")
            print(f"✅ 복사 완료: {filename} → {new_filename}")
    except Exception as e:
        print(f"❌ 변환 실패: {filename} / {e}")
