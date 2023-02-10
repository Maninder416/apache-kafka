FROM adoptopenjdk/openjdk11:alpine-jre

COPY target/springboot-kafka.jar app.jar

ENTRYPOINT["java","-jar","/app.jar"]