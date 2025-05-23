# 훈련 학습 결과 메모

## AI 생성 이미지 분류 모델 학습 진행도
1. efficient_real_fake_v1

    데이터셋 출처 : https://www.kaggle.com/datasets/tristanzhang32/ai-generated-images-vs-real-images / 총 60,000장(1:1 비율)
    
    결과 : Epoch 15, Loss: 0.0045 -> 80%까지 진행 

2. efficient_real_fake_v2

        파인튜닝 결과 추가 Epoch 4, 84% 정확도 
        🕒 Epoch 18 시작 - 현재 시간: 2025-05-06 04:55:44
        ✅ Model saved to: efficientnet_real_fake.pth
        🧠 Best model updated (Loss: 0.0028)
        Epoch 18, Loss: 0.0028, Time: 355m 52s
        Validation Accuracy: 0.9769, Loss: 0.1246

3. gen_v3

        🕒 Epoch 6 시작 - 현재 시간: 2025-05-18 13:45:51
        ✅ Model saved to: gen_v3.pth
        🧠 Best model updated (Loss: 0.0239)
        Epoch 6, Loss: 0.0239, Time: 197m 42s
        Validation Accuracy: 0.9724, Loss: 0.0971
        

## 합성 사진 분류 모델 학습 진행도
1. tampering_v1

        첫번째 학습결과 : 정확도 27% ... 검증할 때 조차 좋지 않은 결과를 보여주고 있음 이유가 뭘까
        🕒 Epoch 16 시작 - 현재 시간: 2025-05-07 08:03:54
        ✅ Model saved to: efficientnet_real_fake.pth
        🧠 Best model updated (Loss: 0.0624)
        Epoch 16, Loss: 0.0624, Time: 57m 32s
        Validation Accuracy: 0.9132, Loss: 0.2660

2. tampering_v2 : 기록을 못함...
