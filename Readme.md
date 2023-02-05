create topic:
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3 --replication-factor 1

List topic:
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

describe topic:

./bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic optum-labs-topic

Delete topic:
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic optum-labs-topic2


how to consumer message on kafka:

./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic optum-labs-topic --from-beginning