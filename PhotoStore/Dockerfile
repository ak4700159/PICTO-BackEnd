# JDK 17 이미지를 기반으로 설정
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사 (./build/libs/에서 생성된 JAR 파일)
COPY build/libs/*.jar app.jar

# 환경변수 설정
ENV SERVER_PORT=8083
ENV SPRING_PROFILES_ACTIVE=prod

# 컨테이너 포트 설정
EXPOSE 8083

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]