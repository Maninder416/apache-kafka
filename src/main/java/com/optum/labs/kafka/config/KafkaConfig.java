package com.optum.labs.kafka.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    public AdminClient adminClient(){
        Properties properties= new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        return AdminClient.create(properties);
    }

    @Bean
    public NewTopic newTopic(){
        return new NewTopic("sentences",1,(short) 1);
    }

    @Bean
    public NewTopic newTopic2(){
        return new NewTopic("word-count",1,(short) 1);
    }
}
