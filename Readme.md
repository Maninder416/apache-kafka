commands to run:
1. It will help us to create image from DockerFile:
    
    mvn clean install

2. Second command is docker-compose:
    docker-compose build

    docker-compose up
3. If everything works fine:
   
   docker images

   docker container ls
4. you can connect MySQL using container name:
   
    docker exec -it springboot-kafka-mysqldb-1 mysql -u root -p
5. you can check container logs using container name:
   
    docker logs springboot-kafka-server-1

By default, our docker MySQL always run on 3306 port but if you want to change to another port then you have to add one property called 'MYSQL_TCP_PORT'. Now our docker MySQL will run on port 3307.

Confluent Kafka Commands:

List:

   kafka-topics --list --bootstrap-server localhost:9092 --list

Create:
   
   kafka-topics --bootstrap-server localhost:9092 --topic first_topic --create --partitions 1 --replication-factor 1

Delete:
   
   kafka-topics --bootstrap-server localhost:9092 --delete --topic first_topic_demo