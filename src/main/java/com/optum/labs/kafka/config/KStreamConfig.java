package com.optum.labs.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.css.Counter;


import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class KStreamConfig {

    @Value("${kafka.schema-registry}")
    public String SCHEMA_REGISTRY_URL;
    @Value("${kafka.bootstrap-server}")
    public String KAFKA_BOOTSTRAP_SERVER;
    @Value("${kafka.application-id}")
    public String PRODUCT_CATEGORY_APP_ID;

    private static final AtomicInteger COUNTER= new AtomicInteger();

    @Bean
    public Properties properties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, PRODUCT_CATEGORY_APP_ID+COUNTER.getAndIncrement());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        return props;
    }

    /**
     * Creating the common topology for all streams
     *
     * @param builder
     */
    public void topology(StreamsBuilder builder) {
        final Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());
        kafkaStreams.cleanUp();
        kafkaStreams.start();
        System.out.println("working it");
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }


}
