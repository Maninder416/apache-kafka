//package com.optum.labs.kafka.stream;
//
//import com.optum.labs.kafka.model.User;
//import org.apache.kafka.streams.kstream.KStream;
//import org.springframework.cloud.stream.annotation.Input;
//import org.springframework.cloud.stream.annotation.Output;
//
//public interface KStreamProcessor {
//
//    @Input("test-producer-consumer")
//    KStream<String,User> input();
//
//    @Output("new-topic")
//    KStream<String,User> output();
//
//
//}
