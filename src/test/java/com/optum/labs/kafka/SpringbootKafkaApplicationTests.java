package com.optum.labs.kafka;

import com.optum.labs.kafka.consumer.AvroConsumerExample;
import com.optum.labs.kafka.producer.AvroProducer;
import com.optum.labs.kafka.repository.EmployeeRepository;

import com.optum.labs.kafka.schema.Employee;
import com.optum.labs.kafka.schema.EmployeeAllDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Slf4j
class SpringbootKafkaApplicationTests {

	@Container
	static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

	@DynamicPropertySource
	public static void initKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
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
		publisher.sendEmployeeBasicDetails(new Employee(1,"maninder","reactivestax"));
		await().pollInterval(Duration.ofSeconds(3))
				.atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
					// assert statement
				});
	}

	@Test
	public void testConsumeEvents() {
		log.info("***** Integration consumer testing ******");
		EmployeeAllDetails employee1 = new EmployeeAllDetails(1,"maninder","reactivestax",123456789,"development","active");
		kafkaTemplate.send("avro-employee-all-details", employee1);
		await().pollInterval(Duration.ofSeconds(3)).atMost(10, SECONDS).untilAsserted(() -> {
			Optional<com.optum.labs.kafka.model.Employee> savedEmployee = employeeRepository.findById(1L);
			Assertions.assertTrue(savedEmployee.isPresent());
			Assertions.assertEquals(1,savedEmployee.get().getId());
			Assertions.assertEquals("maninder",savedEmployee.get().getName());
			Assertions.assertEquals("reactivestax",savedEmployee.get().getCompany());
			Assertions.assertEquals(123456789,savedEmployee.get().getSin());
			Assertions.assertEquals("development",savedEmployee.get().getDepartment());
			Assertions.assertEquals("active",savedEmployee.get().getStatus());
		});
	}

}





