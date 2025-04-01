FROM python:3.8

WORKDIR /preprocessing

RUN apt-get update && apt-get install -y \
   libgl1-mesa-glx \
   libglib2.0-0 \
   libsm6 \
   libxext6 \
   libxrender-dev \
   && rm -rf /var/lib/apt/lists/*

COPY PreProcessing.txt .
RUN pip install --no-cache-dir -r PreProcessing.txt

COPY person_detection.pth .
COPY resnet50_places365.pth.tar .
COPY person_detector.py .
COPY nsfw_detector.py .
COPY text_detector.py .
COPY get_tag.py .
COPY PhotoPreProcessing.py .

RUN mkdir uploads
ENV STORE_SERVER_URL=http://52.78.237.242:8084/photo-store/photos

EXPOSE 8083

CMD ["python", "PhotoPreProcessing.py"]
