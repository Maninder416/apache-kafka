docker exec -it  broker kafka-topics --create --topic customer-details  --bootstrap-server localhost:9092

docker exec -it  broker kafka-topics --create --topic transaction-details  --bootstrap-server localhost:9092

docker exec -it  broker kafka-topics --create --topic customer-transaction-details  --bootstrap-server localhost:9092