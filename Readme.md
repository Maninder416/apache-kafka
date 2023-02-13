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

   