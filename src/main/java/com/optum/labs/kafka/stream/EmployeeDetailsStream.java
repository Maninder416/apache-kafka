package com.optum.labs.kafka.stream;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.schema.Employee;
import com.optum.labs.kafka.schema.EmployeeAllDetails;
import com.optum.labs.kafka.schema.EmployeeJobDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

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

    public void employeeStream() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Employee> employeeKStream = builder.stream(employeeBasicDetails);
        employeeKStream.print(Printed.toSysOut());
        employeeKStream.foreach(((key, value) ->
                log.info("***** Key value for employee basic details ***** : :{} :{}", key, value)));

        KStream<String, Employee> employeeInfoStream = employeeKStream.selectKey((key, value) ->
                value.getId().toString());

        KStream<String, EmployeeJobDetails> employeeJobDetailsKStream = builder.stream(employmentDetails);
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
        employeeAllDetailsKStream.to(employeeAllDetails);
        kStreamConfig.topology(builder);
    }
}
