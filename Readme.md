# Kafka Avro producer and consumer

```bash
In this project, we are doing kafka streaming, sending data to 2 topics and joining this data and sending it to the
third topic and consuming it and saving into MySQL DB.

1. docker-compose up
2. run the spring boot app
3. Post the avro object on :  "localhost:9000/sendStockHistory"

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

here is a diagram for more explaination:

<p align="center">
  <img src="/Users/manindersingh/Documents/spring-boot-projects/springboot-kafka/source/img.png" width="350" title="hover text">
  <img src="/Users/manindersingh/Documents/spring-boot-projects/springboot-kafka/source/img.png" width="350" alt="accessibility text">
</p>
 
 
```
