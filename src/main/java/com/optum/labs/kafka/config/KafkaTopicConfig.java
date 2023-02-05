package com.optum.labs.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name("optum-labs-topic")
//                .partitions(3): we can pass partitions like this but in this example,
//                //we will go with default option
                .build();
    }
}
