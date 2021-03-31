FROM java:8
LABEL maintainer="usmnhyder@live.com"
WORKDIR /app
COPY target/rest-service-0.0.1-SNAPSHOT.jar /app/spring-boot-app.jar
ENTRYPOINT ["java","-jar","spring-boot-app.jar"]