//package com.optum.labs.kafka.config;
//import java.util.Properties;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.apache.kafka.connect.runtime.ConnectorConfig;
//import org.apache.kafka.connect.runtime.WorkerConfig;
//import org.apache.kafka.connect.storage.MemoryOffsetBackingStore;
//import org.apache.kafka.connect.storage.StringConverter;
//import org.apache.kafka.connect.tools.GenericStandaloneConfig;
//
//
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class KafkaConnectorConfig {
//
//    public void configuration(){
//        // Set up connector configuration
//        Properties props = new Properties();
//        props.put(ConnectorConfig.NAME_CONFIG, "mysql-source-connector");
//        props.put(ConnectorConfig.CONNECTOR_CLASS_CONFIG, "io.confluent.connect.jdbc.JdbcSourceConnector");
//        props.put(ConnectorConfig.TASKS_MAX_CONFIG, "1");
//        props.put("connection.url", "jdbc:mysql://localhost:3306/silicon");
//        props.put("connection.user", "root");
//        props.put("connection.password", "");
//        props.put("mode", "timestamp");
//        props.put("timestamp.column.name", "mytimestamp");
//        props.put("table.whitelist", "clients");
//        props.put("topic.prefix", "mysql-");
//
//        // Set up Kafka producer configuration
//        Properties producerProps = new Properties();
//        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//        // Create a standalone Kafka Connect instance with the connector configuration and producer configuration
//        GenericStandaloneConfig config = new GenericStandaloneConfig(props, producerProps, new MemoryOffsetBackingStore());
//
//        // Start the Kafka Connect instance
//        config.start();
//        System.out.println("MySQL source connector started!");
//
//    }
//}
