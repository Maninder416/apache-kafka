//package com.optum.labs.kafka.stream;
//
//import com.optum.labs.kafka.model.User;
//import org.apache.kafka.streams.kstream.KStream;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.messaging.handler.annotation.SendTo;
//
//@EnableBinding(KStreamProcessor.class)
//public class KafkaStreamFilter {
//
//    @StreamListener("test-producer-consumer")
//    @SendTo("new-topic")
//    public KStream<String, User> process(KStream<String,User> input){
//        return input.filter(((key, value) -> value.getFirstName().equalsIgnoreCase("maninder")));
//    }
//}
