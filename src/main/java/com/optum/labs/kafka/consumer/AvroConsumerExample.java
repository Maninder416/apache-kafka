package com.optum.labs.kafka.consumer;

import com.optum.labs.kafka.model.Employee;
import com.optum.labs.kafka.repository.EmployeeRepository;
import com.optum.labs.kafka.schema.EmployeeAllDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AvroConsumerExample {
    @Autowired
    private EmployeeRepository employeeRepository;

    @KafkaListener(topics = "${avro.topic.employee-all-details}", containerFactory = "kafkaListenerContainerFactory")
    public void read(ConsumerRecord<String, EmployeeAllDetails> record) {
        String key = record.key();
        EmployeeAllDetails employeeAllDetails = record.value();
        Employee employee = new Employee();
        employee.setEmpId(employeeAllDetails.getId());
        employee.setName(employeeAllDetails.getName().toString());
        employee.setCompany(employeeAllDetails.getCompany().toString());
        employee.setSin(employeeAllDetails.getSin());
        employee.setStatus(employeeAllDetails.getStatus().toString());
        employee.setDepartment(employeeAllDetails.getDepartment().toString());
        employeeRepository.save(employee);
        log.info("Key and value for avro received message is :{} :{} ", key, employeeAllDetails.toString());

    }
}
