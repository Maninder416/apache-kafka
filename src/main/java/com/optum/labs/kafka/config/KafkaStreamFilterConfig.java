package com.optum.labs.kafka.config;

import com.optum.labs.kafka.model.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class KafkaStreamFilterConfig {


    @Value("${spring.kafka.producer.properties.schema.registry.url}")
    public String SCHEMA_REGISTRY_URL;
    @Value("${spring.kafka.bootstrap-servers}")
    public String KAFKA_BOOTSTRAP_SERVER;
    @Value("${spring.kafka.consumer.group-id}")
    public String PRODUCT_CATEGORY_APP_ID;

    private static final AtomicInteger COUNTER= new AtomicInteger();

    @Bean
    public Properties properties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, PRODUCT_CATEGORY_APP_ID+COUNTER.getAndIncrement());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
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
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }







    @Bean
    public StreamsBuilder kStream(){
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String,User> stream = builder.stream("test-producer-consumer");
        stream.filter((key,value)->value.getFirstName().equalsIgnoreCase("maninder"))
                .to("new-topic");
        return builder;
    }
}
