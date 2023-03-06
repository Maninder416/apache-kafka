package com.optum.labs.kafka.controller;

import com.optum.labs.kafka.producer.AvroProducer;
import com.optum.labs.kafka.schema.Employee;
import com.optum.labs.kafka.schema.EmployeeJobDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SpringAvroController {
    @Autowired
    private AvroProducer avroProducer;

    @PostMapping(value = "/employee")
    public ResponseEntity<String> sendMessage(@RequestBody Employee employee) {
        log.info("Payload here is: " + employee.toString());
        Employee employee1 = Employee.newBuilder().build();
        employee1.setName(employee.getName());
        employee1.setCompany(employee.getCompany());
        employee1.setId(employee.getId());
        avroProducer.sendEmployeeBasicDetails(employee1);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Employee basic details Avro message send");
    }

    @PostMapping(value = "/jobDetails")
    public ResponseEntity<String> jobDetails(@RequestBody EmployeeJobDetails jobDetails) {
        log.info("Payload here is: " + jobDetails.toString());
        EmployeeJobDetails details = EmployeeJobDetails.newBuilder().build();
        details.setDepartment(jobDetails.getDepartment());
        details.setSin(jobDetails.getSin());
        details.setId(jobDetails.getId());
        details.setStatus(jobDetails.getStatus());
        avroProducer.sendEmploymentDetails(details);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Employee job details Avro message send");
    }


}
