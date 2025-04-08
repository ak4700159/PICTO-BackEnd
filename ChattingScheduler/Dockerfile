FROM amazoncorretto:17

CMD ["./gradlew", "clean", "build"]

VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8085

ENTRYPOINT ["java","-jar","/app.jar"]