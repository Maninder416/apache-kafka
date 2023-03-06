package com.optum.labs.kafka.stream;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.schema.Employee;
import com.optum.labs.kafka.schema.EmployeeAllDetails;
import com.optum.labs.kafka.schema.EmployeeJobDetails;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
@Slf4j
public class EmployeeDetailsStream {

    @Value("${avro.topic.employee-basic-details}")
    private String employeeBasicDetails;
    @Value("${avro.topic.employee-employment-details}")
    private String employmentDetails;
    @Value("${avro.topic.employee-all-details}")
    private String employeeAllDetails;
    @Autowired
    private KStreamConfig kStreamConfig;

    @Value("${spring.kafka.producer.properties.schema.registry.url}")
    public String SCHEMA_REGISTRY_URL;

    public void employeeStream() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Employee> employeeSerde = new SpecificAvroSerde<>();
        employeeSerde.configure(Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,SCHEMA_REGISTRY_URL),false);
        KStream<String, Employee> employeeKStream = builder.stream(employeeBasicDetails, Consumed.with(stringSerde, employeeSerde));
        employeeKStream.print(Printed.toSysOut());
        employeeKStream.foreach(((key, value) ->
                log.info("***** Key value for employee basic details ***** : :{} :{}", key, value)));

        KStream<String, Employee> employeeInfoStream = employeeKStream.selectKey((key, value) ->
                value.getId().toString());

        final Serde<EmployeeJobDetails> employeeJobDetailsSerde = new SpecificAvroSerde<>();
        employeeJobDetailsSerde.configure(Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,SCHEMA_REGISTRY_URL),false);
        KStream<String, EmployeeJobDetails> employeeJobDetailsKStream = builder.stream(employmentDetails, Consumed.with(stringSerde, employeeJobDetailsSerde));
        employeeJobDetailsKStream.foreach(((key, value) ->
                log.info("***** Employee job details ****** : :{} :{} ", key, value)
        ));
        KStream<String, EmployeeJobDetails> employeeJobDetailInfo = employeeJobDetailsKStream.selectKey((key, value) ->
                value.getId().toString());

        ValueJoiner<Employee, EmployeeJobDetails, EmployeeAllDetails> joiner =
                (employee, details) -> EmployeeAllDetails.newBuilder()
                        .setId(employee.getId())
                        .setCompany(employee.getCompany())
                        .setName(employee.getName())
                        .setDepartment(details.getDepartment())
                        .setSin(details.getSin())
                        .setStatus(details.getStatus())
                        .build();

        KStream<String, EmployeeAllDetails> employeeAllDetailsKStream = employeeInfoStream.join(employeeJobDetailInfo
                , joiner,
                JoinWindows.of(Duration.ofSeconds(3000)));

        employeeAllDetailsKStream.print(Printed.toSysOut());
        employeeAllDetailsKStream.foreach(((key, value) ->
                log.info("***** Employee all details: *****: :{} :{} ", key, value)));
        final Serde<EmployeeAllDetails> employeeAllDetailsSerde= new SpecificAvroSerde<>();
        employeeAllDetailsSerde.configure(Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,SCHEMA_REGISTRY_URL),false);
        employeeAllDetailsKStream.to(employeeAllDetails,Produced.with(stringSerde,employeeAllDetailsSerde));
        kStreamConfig.topology(builder);
    }
}
