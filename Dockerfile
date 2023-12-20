FROM amazoncorretto:17

ARG JAR_FILE=/build/libs/common-blog-project-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.include=prod", "-jar", "app.jar"]