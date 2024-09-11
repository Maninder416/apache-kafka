package com.sunlife.kafka.json.config;

import com.github.javafaker.Faker;
import com.sunlife.kafka.json.model.Phone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class FakerConfig {

    @Bean
    public Faker faker(){
        return new Faker();
    }
}
