package com.optum.labs.kafka;

import com.optum.labs.kafka.config.KafkaProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootKafkaApplication implements CommandLineRunner {

	@Autowired
	KafkaProducerConfig kafkaProducerConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootKafkaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Bean is: "+kafkaProducerConfig.toString());
	}
}
