package com.optum.labs.kafka;

import com.github.javafaker.Faker;
import com.optum.labs.kafka.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootKafkaApplication implements CommandLineRunner {

    @Autowired
    Faker faker;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootKafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("faker bean: "+faker);
    }
}
