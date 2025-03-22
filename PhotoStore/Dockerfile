# 베이스 이미지
FROM bellsoft/liberica-openjdk-alpine:18

# clean : 이전 빌드 결과 삭제 / build : 프로젝트 빌드하여 .jar 파일 생성
CMD ["./gradlew", "clean", "build"]

# 임시 디렉토리 설정
VOLUME /tmp

# JAR_FILE : gradle 빌드 결과물이 위치하는 경로 지정
ARG JAR_FILE=build/libs/*.jar

# 로컬 파일->컨테이너 내부로 복사
COPY ${JAR_FILE} app.jar

# 컨테이너 내부에서 외부로 개방할 포트 지정
EXPOSE 8086

# 컨테이너 실행 시 jar 파일 실행
ENTRYPOINT ["java","-jar","/app.jar"]