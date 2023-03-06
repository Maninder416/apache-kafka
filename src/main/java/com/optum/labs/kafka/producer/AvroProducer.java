package com.optum.labs.kafka.producer;

import com.optum.labs.kafka.schema.Employee;
import com.optum.labs.kafka.schema.EmployeeJobDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Service
@Slf4j
public class AvroProducer {

    @Value("${avro.topic.employee-basic-details}")
    private String employeeBasicDetails;

    @Value("${avro.topic.employee-employment-details}")
    private String employmentDetails;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmployeeBasicDetails(Employee employee) {
        ListenableFuture<SendResult<String, Object>> employeeDetails =
                kafkaTemplate.send(employeeBasicDetails, String.valueOf(employee.getId()), employee);
        employeeDetails.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Message failed to produce");

            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Avro message successfully send");
            }
        });

    }

    public void sendEmploymentDetails(EmployeeJobDetails jobDetails) {
        ListenableFuture<SendResult<String, Object>> employeeDetails =
                kafkaTemplate.send(employmentDetails, String.valueOf(jobDetails.getId()), jobDetails);
        employeeDetails.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Message failed to produce");

            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Avro message successfully send");
            }
        });

    }

}
