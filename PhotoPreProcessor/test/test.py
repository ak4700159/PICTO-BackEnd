import os
import shutil
import logging
import random
from concurrent.futures import ThreadPoolExecutor
import sys

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from src.detector.nsfw_detector import NSFWDetector
from src.detector.tampering_detector import TamperingDetector
from src.detector.person_detector import PersonDetector
from src.detector.text_detector import TextDetector
from src.detector.ai_generation_detector import GenDetector

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 디텍터 리스트 (필요 시 주석 해제)
# detectors = {
#     "nsfw": NSFWDetector(),
#     "tampering": TamperingDetector(),
#     "person": PersonDetector("../src/person_detection.pt"),
#     "text": TextDetector(),
#     "gen": GenDetector(),
# }

# 현재는 tampering과 gen만 활성화
detectors = {
    "tam": TamperingDetector(),
    "gen": GenDetector(),
}

input_dir = "./real_input"
output_dir = "./output"
categories = ["nsfw", "tam", "person", "text", "gen", "real"]

# output 폴더 정리
def clear_output():
    for category in categories:
        dir_path = os.path.join(output_dir, category)
        if os.path.exists(dir_path):
            shutil.rmtree(dir_path)
        os.makedirs(dir_path)
    logger.info("✅ output 폴더 초기화 완료.")

# 단일 이미지 처리
def process_image(image_name):
    image_path = os.path.join(input_dir, image_name)
    max_score = 0.0
    selected_label = "real"

    for label, detector in detectors.items():
        try:
            score = detector.detect(image_path)
            logger.info(f"{image_name} → {label} score: {score:.3f}")
            if score > max_score:
                max_score = score
                selected_label = label
        except Exception as e:
            logger.error(f"{label} 디텍터 오류: {e}")

    if max_score < 0.8:
        selected_label = "real"

    dest_path = os.path.join(output_dir, selected_label, image_name)
    shutil.copy(image_path, dest_path)

    return image_name, selected_label

# 사용자 입력에 따라 무작위 샘플 선택 및 테스트
def run_test():
    logger.info("🚀 테스트 시작")
    try:
        num_samples = int(input("테스트할 이미지 수를 입력하세요 (예: 100): "))
    except ValueError:
        logger.error("❌ 잘못된 입력입니다. 숫자를 입력해주세요.")
        return

    image_files = [f for f in os.listdir(input_dir) if f.lower().endswith(('.jpg', '.tif'))]
    if num_samples > len(image_files):
        logger.warning(f"⚠️ 요청한 수가 전체 수({len(image_files)})보다 많습니다. 전체 이미지를 사용합니다.")
        num_samples = len(image_files)

    selected_files = random.sample(image_files, num_samples)
    total = len(selected_files)
    correct = 0
    results = []

    for fname in selected_files:
        image_name, predicted = process_image(fname)
        label = fname.split("_")[0].lower()
        if label == "generation":
            label = "gen"

        if label == predicted:
            correct += 1
        results.append((image_name, predicted))
        logger.info(f"{image_name} process completed! save at {(image_name, predicted)}\n")

    acc = (correct / total) * 100
    logger.info(f"✅ 정확도: {acc:.2f}% ({correct}/{total})")

    with open("result_log.txt", "w", encoding="utf-8") as f:
        for name, pred in results:
            f.write(f"{name},{pred}\n")
        f.write(f"\n정확도: {acc:.2f}%")

# 메뉴 출력
def menu():
    print("\n[ PICTO TEST MENU ]")
    print("1. 출력물 비우기")
    print("2. 테스트 실행")
    print("3. 종료")
    return input("[ INPUT ] 번호 입력: ")

if __name__ == '__main__':
    logger.info("📌 PICTO VALIDATION MODEL TEST START")

    while True:
        choice = menu()
        if choice == "1":
            clear_output()
        elif choice == "2":
            run_test()
        elif choice == "3":
            print("종료합니다.")
            break
        else:
            print("❌ 잘못된 입력입니다.")