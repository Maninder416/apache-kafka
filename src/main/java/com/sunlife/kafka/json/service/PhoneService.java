package com.sunlife.kafka.json.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.sunlife.kafka.json.model.Phone;
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
    private KafkaTemplate<String, Phone> kafkaTemplate;

    @Autowired
    private Faker faker;

    private int loopCount=10;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generatePhone(){

        for (int i= 1; i<=loopCount;i++) {
            Phone phone = new Phone();
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

    public void sendPhonesToKafka() {
        Iterable<Phone> phones = phoneRepository.findAll();
        for (Phone phone : phones) {
            System.out.println("Printing time");
            System.out.println("Processing phone: " + phone);
            // Serialize Phone object to JSON string
            String phoneJson = serializePhoneToJson(phone);
            System.out.println("Serialized JSON: " + phoneJson);
            String lowerCaseJson = convertJsonKeysToLowerCase(phoneJson);
            System.out.println("Lowercase JSON: " + lowerCaseJson);
            Phone phoneWithLowerCaseKeys = deserializeJsonToPhone(lowerCaseJson);
            kafkaTemplate.send("demo", phoneWithLowerCaseKeys);
        }
    }

    private Phone deserializeJsonToPhone(String json) {
        try {
            return objectMapper.readValue(json, Phone.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String serializePhoneToJson(Phone phone) {
        // Use your preferred JSON library to serialize the Phone object to a JSON string
        // Example using Jackson:
//        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(phone);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert JSON keys to lowercase
    private String convertJsonKeysToLowerCase(String json) {
//        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode lowerCaseNode = convertJsonNodeKeysToLowerCase(rootNode);
            return objectMapper.writeValueAsString(lowerCaseNode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JsonNode convertJsonNodeKeysToLowerCase(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            ObjectNode newObjectNode = objectMapper.createObjectNode();
            objectNode.fieldNames().forEachRemaining(fieldName -> {
                JsonNode valueNode = objectNode.get(fieldName);
                newObjectNode.set(fieldName.toLowerCase(), convertJsonNodeKeysToLowerCase(valueNode));
            });
            return newObjectNode;
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            ArrayNode newArrayNode = objectMapper.createArrayNode();
            for (JsonNode item : arrayNode) {
                newArrayNode.add(convertJsonNodeKeysToLowerCase(item));
            }
            return newArrayNode;
        } else {
            return node;
        }
    }
}
