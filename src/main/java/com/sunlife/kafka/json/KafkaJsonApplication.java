package com.sunlife.kafka.json;

import com.sunlife.kafka.json.service.PhoneService;
import com.sunlife.kafka.json.topology.PhoneTopologyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaJsonApplication implements CommandLineRunner {

	@Autowired
	private PhoneService phoneService;
	@Autowired
	private PhoneTopologyWrapper phoneTopologyWrapper;

	public static void main(String[] args) {
		SpringApplication.run(KafkaJsonApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("calling this method");
		phoneService.generatePhone();
		phoneTopologyWrapper.creditLineDetails();
//		phoneService.sendPhonesToKafka();
	}
}
