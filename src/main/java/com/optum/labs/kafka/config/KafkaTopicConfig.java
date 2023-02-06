package com.optum.labs.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic}")
    private String topicName;
    @Value("${spring.kafka.jsonTopic}")
    private String jsonTopicName;
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(topicName)
//                .partitions(3): we can pass partitions like this but in this example,
//                //we will go with default option
                .build();
    }

    /**
     * creating topic for sending json object
     * @return
     */
    @Bean
    public NewTopic jsonTopic() {
        return TopicBuilder.name(jsonTopicName)
                .build();
    }
}
