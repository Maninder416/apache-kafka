package com.optum.labs.kafka;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.stream.EmployeeDetailsStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
@SpringBootApplication
@EnableKafka
@Slf4j
public class SpringbootKafkaApplication implements CommandLineRunner {

    @Autowired
    private KStreamConfig kStreamConfig;

    @Autowired
    private EmployeeDetailsStream employeeDetailsStream;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootKafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        employeeDetailsStream.employeeStream();
    }
}
