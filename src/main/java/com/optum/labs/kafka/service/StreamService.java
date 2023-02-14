package com.optum.labs.kafka.service;

import com.optum.labs.kafka.model.Order;
import com.optum.labs.kafka.streams.StreamBindings;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding(StreamBindings.class)
public class StreamService {

    @StreamListener("order-input-channel")
    @SendTo("order-takeaway-output-channel")
    public KStream<String, Order> takeAway(KStream<String, Order> order){
        return order.filter((k,v)->v.getDeliveryType().equalsIgnoreCase("takeaway"));
    }

    @StreamListener("order-input-channel")
    @SendTo("order-home-delivery-output-channel")
    public KStream<String, Order> homeDelivery(KStream<String, Order> order){
        return order.filter((k,v)->v.getDeliveryType().equalsIgnoreCase("homedelivery"));
    }




}
