package com.sunlife.kafka.json.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.sunlife.kafka.json.jpa.PhoneObject;
import com.sunlife.kafka.json.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private KafkaTemplate<String, PhoneObject> kafkaTemplate;

    @Autowired
    private Faker faker;

    private int loopCount=1000;

    public String generatePhone(){

        for (int i= 1; i<=loopCount;i++) {
            PhoneObject phone = new PhoneObject();
            phone.setPartyId(faker.number().randomNumber());
            phone.setPhoneNum(faker.phoneNumber().cellPhone());
            phone.setPhoneAreaCd(faker.phoneNumber().extension());
            phone.setFirstName(faker.name().firstName());
            phone.setCifUpdtTmstmp(faker.date().birthday().toString());
            phone.setCifCreatTmstmp(faker.date().birthday().toString());
            phoneRepository.save(phone);
        }
        return "Phone data has been generated";
    }

}
