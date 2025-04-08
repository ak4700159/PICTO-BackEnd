import easyocr

# 결과값에 대해 더 자세히 알아봐야됨
class TextDetector:
    def __init__(self):
        self.languages = ['en', 'ko']
        self.reader = easyocr.Reader(self.languages)

    def detect(self, image_path):
        results = self.reader.readtext(image_path)
        return len(results) > 0