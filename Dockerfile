FROM openjdk:11
COPY target/spring-kafka.jar spring-kafka.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar","spring-kafka.jar"]