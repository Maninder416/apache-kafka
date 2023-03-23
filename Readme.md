# Commands to run:

```bash
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
   
producer:

kafka-console-producer --topic testing --bootstrap-server localhost:9092

How to run this project:

1. Run the docker-compose file.
2. Run the spring boot app. it will create your required topics that you need.
3. Make sure your connectors are up and working. If it does not configure go to path
 "/source/docker/" and run script "script.sh".
4. Make sure, go to control center and check if all the connectors are up.
5. For testing purpose to confirm, if calculations are done properly,
I have prepared one testing stream.To check it, first you need to run the method
 "dataCreationService.generateCanDeleteData()". It will generate some
data and will send the data to topic. Make sure KafkaTemplate line in this code 
is not commented out. I have commented it, because I already sent data and
I dont want to send the data again and again.
6. After that, hit the GET API the "http://localhost:9001/result?startDate=2022-01-01&endDate=2022-01-15"
7. It will generate the fake data between the dates we define in API and replace
the actual data on the specific dates which we are getting from topic that
we defined in step5. Once the data is prepared with actual data and fake
data, just send this data to the another topic. 
For example, I am sending this data to topic "test5"
8. Create the KSQL stream in KSQL DB with the topic "test5" so that whenever the
data we send it will save into this KStream.
Sample to create KStream:
how to create json steam in ksql db in kafka:

CREATE STREAM myJsonStream (
  ID BIGINT,
  POSTDATE VARCHAR,
  EFFECTIVEDATE VARCHAR,
  AMOUNT DOUBLE,
  ACCOUNTBALANCE DOUBLE

) WITH (
  KAFKA_TOPIC='topic5',
  VALUE_FORMAT='JSON'
);

  ![sample.png](sample.png)
  
  this image is available under "source/sample-output" folder

==========================================================================================================

That was for testing purpose but if you want to see the actual data then you can do the following steps:

1. Make sure all the streams are up and working. We have mentioned all the streams in Main class.
2. Make sure your Application port is up and working.
3. Same scenario, like above it will receive the data from topic 15.
topic name is: credit.creditlines.flex-creditline-and-activity-and-loantxn-and-prodcat.out.
and we will pass the date from Rest API: "http://localhost:9001/final?startDate=2022-01-01&endDate=2022-01-15"
and it will generate the fake data and use actual data whatever is coming from the topic.
4. Create the Stream like we created in above step in KSQL:
5. Sample for it:

CREATE STREAM finalOutputStream (
ID BIGINT,
CUST_LINE_NBR VARCHAR,
POSTDT VARCHAR,
CIF VARCHAR,
EFFDT VARCHAR,
FLEX_CMTMNT_AMT_LCY DOUBLE,
FLEX_CMTMNT_AMT_TCY VARCHAR,
FLEX_UNCMTMNT_AMT_LCY VARCHAR,
FLEX_UNCMTMNT_AMT_TCY VARCHAR,
FLEX_FEE_PCT DOUBLE,
FLEX_FEE_ACCR_BAS VARCHAR,
TRANS_CRRNCY_CD VARCHAR,
ENTITY VARCHAR,
APPLID VARCHAR,
SRC_UPDT_DT VARCHAR,
DW_CREATE_TS VARCHAR,
CREATED_BY VARCHAR,
APPLID_LOAN VARCHAR,
CREDIT_LINE_STATUS VARCHAR,
PSGL_DEPARTMENT VARCHAR,
BRANCHNBR VARCHAR,
CBS_AOTEAMCD VARCHAR,
NAMEADDRLN1 VARCHAR,
NAMEADDRLN2 VARCHAR,
NAMEADDRLN3 VARCHAR,
NAMEADDRLN4 VARCHAR,
NAMEADDRLN5 VARCHAR,
NAMEADDRLN6 VARCHAR,
ZIPPOSTALCD VARCHAR,
FULL_NAME VARCHAR,
STATUSCD VARCHAR,
EXPIRYDT VARCHAR,
ACCTNBR VARCHAR,
TRANID VARCHAR,
NOTEPRNCPLBALGROSS DOUBLE,
TRANS_CURRENCY_CODE VARCHAR,
PRODUCT_CD VARCHAR,
PRODUCT_CATEGORY_CD VARCHAR,
ACCOUNT_BALANCE DOUBLE) WITH (
KAFKA_TOPIC='final-topic',
VALUE_FORMAT='JSON'
);
  
  
6. Connect to KSQL DB in command line and run command:

select * from finalOutputStream;

7. Output screenshot is present here:   "source/sample-output/final-topic" folder







```

