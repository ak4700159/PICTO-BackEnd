import os
import shutil

# AI 생성 이미지가 모여 있는 폴더
dataset_dir = "./real_data"
# 이름 변경 후 저장할 폴더
output_dir = "./real_input"

# 출력 폴더 준비
if not os.path.exists(output_dir):
    os.makedirs(output_dir)

# 파일 목록 수집 (jpg, tif만)
ai_images = [f for f in os.listdir(dataset_dir) if f.lower().endswith(('.jpg', '.tif', '.png'))]
ai_images.sort()  # 순서를 일정하게 유지 (필요 시 무작위로 변경 가능)

# 시작 인덱스 설정
start_idx = 801  # 예: gen_0601부터 시작

# 이름 변경 및 복사
for i, fname in enumerate(ai_images):
    ext = os.path.splitext(fname)[1].lower()
    new_name = f"real_{start_idx + i:04d}{ext}"

    src_path = os.path.join(dataset_dir, fname)
    dst_path = os.path.join(output_dir, new_name)

    try:
        shutil.copy2(src_path, dst_path)  # 메타데이터 보존
    except Exception as e:
        print(f"❌ 오류: {fname} 복사 실패 → {e}")

print("✅ 이미지 이름 변경 완료!")