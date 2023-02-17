package com.optum.labs.kafka.service;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ProcessorService {

    @Autowired
    public void process(StreamsBuilder streamsBuilder){
        final Serde<Integer> integerSerde= Serdes.Integer();
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        KStream<Integer,String> textLines= streamsBuilder
                .stream("input-topic2",
                        Consumed.with(integerSerde,stringSerde));
        KTable<String,Long> wordCounts= textLines.flatMapValues(value-> Arrays.asList(
                value.toLowerCase().split("\\W+")))
                .groupBy((key,value)->value, Grouped.with(stringSerde,stringSerde))
                .count();
        wordCounts.toStream().to("output-topic2", Produced.with(stringSerde,longSerde));
    }
}
