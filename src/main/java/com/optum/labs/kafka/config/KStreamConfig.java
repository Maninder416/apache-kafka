package com.optum.labs.kafka.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Properties;

@Configuration
public class KStreamConfig {

    private String brokers= "localhost:9092";
    private String applicationId= "merge-topics-merge";

    @Bean
    public KStream<String,String> kStream(){
        Properties props= new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,brokers);

        StreamsBuilder builder= new StreamsBuilder();
        KStream<String,String> inputStream1= builder.stream("credit.creditlines-product-code.in");
        KStream<String,String> inputStream2= builder.stream("credit.creditlines.product-category.in");
        KStream<String, String> mergedStream = inputStream1.merge(inputStream2);
        mergedStream.to("product-category-code.out", Produced.with(Serdes.String(),Serdes.String()));
        return mergedStream;

//        Topology topology= builder.build();
//        KafkaStreams streams= new KafkaStreams(topology,props);
//        streams.start();
    }
}
