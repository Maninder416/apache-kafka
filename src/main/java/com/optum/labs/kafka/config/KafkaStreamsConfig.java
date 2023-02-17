package com.optum.labs.kafka.config;

import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.TopicBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {

    private String bootstrapServers= "localhost:9092";

    private String applicationId="new-application-id";

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfig2(){
        Map<String,Object> properties= new HashMap<>();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,applicationId);
        properties.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        return new KafkaStreamsConfiguration(properties);
    }

    @Bean
    public KStream<Integer,String> kStreamTest(StreamsBuilder streamsBuilder){
        KStream<Integer,String> kStream= streamsBuilder.stream("input-topic2");
        kStream.mapValues((ValueMapper<String,String>) String::toUpperCase)
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofMillis(1000)))
                .reduce((String value1, String value2)-> value1+value2,Named.as("windowStore"))
                .toStream()
                .map((windowedId, value)->new KeyValue<>(windowedId.key(),value))
                .filter((i,s)->s.length()>40)
                .to("output-topic2");
        kStream.print(Printed.toSysOut());
        return kStream;
    }

    @Bean
    public NewTopic count(){
        return TopicBuilder.name("input-topic2")
                .build();
    }

    @Bean
    public NewTopic count2(){
        return TopicBuilder.name("output-topic2")
                .build();
    }


}
