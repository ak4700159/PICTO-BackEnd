from PIL import Image


# 모든 이미지를 정사각형 사이즈로 만들기 
def pad_to_square(image):
    w, h = image.size
    max_wh = max(w, h)
    pad_left = (max_wh - w) // 2
    pad_top = (max_wh - h) // 2
    result = Image.new("RGB", (max_wh, max_wh), (255,255,255))  # 검정 배경
    result.paste(image, (pad_left, pad_top))
    return result
