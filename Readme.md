Start Confluent Kafka using docker compose

docker-compose up
Create Topics and Avro Schema
./scripts/create-topics-schemas.sh


kafka-avro-console-producer --broker-list localhost:9092 --topic customer-details --property value.schema='{"namespace":"com.ibm.gbs.schema","type": "record","name": "Customer","fields": [{"name": "customerId","type": "string"},{"name": "name","type" :"string"},{"name": "phoneNumber","type": "string"},{"name": "accountId","type": "string"}]}'

Payload:
{"customerId”:”six”,”name":"Maninder","phoneNumber":"123456","accountId":"one"}

kafka-avro-console-producer --broker-list localhost:9092 --topic transaction-details --property value.schema='{"namespace":"com.ibm.gbs.schema","type": "record","name": "Transaction","fields":[{"name": "balanceId","type": {"avro.java.string": "String","type": "string"}},{"name": "accountId","type":{"avro.java.string": "String","type": "string"}},{"name": "balance","type": "float"}]}'

payload:

{"balanceId":"one","accountId":"one","balance":10.99}