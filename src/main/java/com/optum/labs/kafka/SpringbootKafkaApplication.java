package com.optum.labs.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class SpringbootKafkaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootKafkaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Properties properties= new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"word-count-app");
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());
		properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG,"0");

		StreamsBuilder builder= new StreamsBuilder();
		builder.<String,String> stream("sentences")
				.flatMapValues((key,value)-> Arrays.asList(value.toLowerCase().split(" ")))
				.groupBy((key,value)->value)
				.count(Materialized.with(Serdes.String(),Serdes.Long()))
				.toStream()
				.to("word-count", Produced.with(Serdes.String(), Serdes.Long()));

		KafkaStreams kafkaStreams= new KafkaStreams(builder.build(),properties);
		kafkaStreams.start();
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

	}
}
