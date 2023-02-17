package com.optum.labs.kafka;

import org.apache.kafka.clients.admin.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableKafkaStreams
public class SpringbootKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootKafkaApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		Properties props = new Properties();
//		props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//		try (AdminClient adminClient = AdminClient.create(props)) {
//			// List all topics
//			ListTopicsResult listTopicsResult = adminClient.listTopics();
//			Collection<TopicListing> topics = listTopicsResult.listings().get();
//
//			// Create a list of topic names
//			final Collection<String> topicNames = new java.util.ArrayList<>(topics.size());
//			for (TopicListing topic : topics) {
//				topicNames.add(topic.name());
//			}
//
//			// Delete all topics
//			if (!topicNames.isEmpty()) {
//				DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(topicNames);
//				deleteTopicsResult.all().get();
//				System.out.println("All topics deleted successfully!");
//			} else {
//				System.out.println("No topics found to delete.");
//			}
//
//		} catch (ExecutionException | InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
}


