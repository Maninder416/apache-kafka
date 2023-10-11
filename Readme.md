# Kafka Avro producer and consumer

```bash
In this project, we are doing kafka streaming, sending data to 2 topics and joining this data and sending it to the
third topic and consuming it and saving into MySQL DB.

1. docker-compose up
2. run the spring boot app
3. Post the avro object on :  http://localhost:9000/employee

payload for first topic: avro-employee-basic-details

{
    "id":107,
    "name": "Maninder",
    "company": "dataguise"
}

payload for second topic: avro-employee-employment-details

{
    "id":107,
    "sin": 12345,
    "department": "dev-team",
    "status": "work-permit"
}

the output will be like this:

{
    "id":107,
    "name": "Maninder",
    "company": "dataguise",
    "sin": 12345,
    "department": "dev-team",
    "status": "work-permit"
}


here is a diagram for more explaination:
/Users/manindersingh/Documents/spring-boot-projects/springboot-kafka/source/img.png

```

# For integration test case, followed this branch:
```shell
https://github.com/Java-Techie-jt/kafka-integration-test/blob/main/kafka-consumer-example/src/main/java/com/javatechie/consumer/KafkaMessageListener.java
```

