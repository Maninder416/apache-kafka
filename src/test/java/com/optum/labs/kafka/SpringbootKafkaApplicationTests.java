package com.optum.labs.kafka;

import com.optum.labs.kafka.consumer.AvroConsumerExample;
import com.optum.labs.kafka.producer.AvroProducer;
import com.optum.labs.kafka.repository.EmployeeRepository;

import com.optum.labs.kafka.schema.Employee;
import com.optum.labs.kafka.schema.EmployeeAllDetails;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Slf4j
class SpringbootKafkaApplicationTests {



	@Container
	static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");


	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username",mySQLContainer::getUsername);
		registry.add("spring.datasource.password",mySQLContainer::getPassword);
	}

	@DynamicPropertySource
	public static void initKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@BeforeAll
	static void  beforeAll(){
		mySQLContainer.start();
	}

	@AfterAll
	static void afterAll(){
		mySQLContainer.stop();
	}

	@Autowired
	private AvroProducer publisher;

	@Autowired
	private AvroConsumerExample consumerExample;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	public void testSendEventsToTopic() {
		log.info("***** Integration producer testing ******");
		System.out.println("jdbc:mysql://localhost:52226/test");
		System.out.println("jdbc url: "+mySQLContainer.getJdbcUrl());
		System.out.println("username: "+mySQLContainer.getUsername());
		System.out.println("password: "+mySQLContainer.getPassword());
		System.out.println("db: "+mySQLContainer.getDatabaseName());
		publisher.sendEmployeeBasicDetails(new Employee(1,"maninder","reactivestax"));
		await().pollInterval(Duration.ofSeconds(3))
				.atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
					// assert statement
				});
	}

	@Test
	public void testConsumeEvents() {
		log.info("***** Integration consumer testing ******");
		EmployeeAllDetails employee1 = new EmployeeAllDetails(2,"rajat","reactivestax",123456789,"development","active");
		kafkaTemplate.send("avro-employee-all-details", employee1);
		await().pollInterval(Duration.ofSeconds(3)).atMost(10, SECONDS).untilAsserted(() -> {
			Optional<com.optum.labs.kafka.model.Employee> savedEmployee = employeeRepository.findById(1L);
			Assertions.assertTrue(savedEmployee.isPresent());
			Assertions.assertEquals(1,savedEmployee.get().getId());
			Assertions.assertEquals("rajat",savedEmployee.get().getName());
			Assertions.assertEquals("reactivestax",savedEmployee.get().getCompany());
			Assertions.assertEquals(123456789,savedEmployee.get().getSin());
			Assertions.assertEquals("development",savedEmployee.get().getDepartment());
			Assertions.assertEquals("active",savedEmployee.get().getStatus());
		});
	}

}





