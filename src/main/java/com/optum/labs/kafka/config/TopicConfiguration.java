package com.optum.labs.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic employeeBasicDetails() {
        return TopicBuilder.name("avro-employee-basic-details")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic EmploymentDetails() {
        return TopicBuilder.name("avro-employee-employment-details")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic employeeAllDetails() {
        return TopicBuilder.name("avro-employee-all-details")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
