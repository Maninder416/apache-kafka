package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.model.Order;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.*;
import org.springframework.cloud.stream.annotation.Input;

public interface StreamBindings {

    @Input("order-input-channel")
    KStream<String, Order> inputStream();

    @Output("order-takeaway-output-channel")
    KStream<String, Order> takeAwayStream();

    @Output("order-home-delivery-output-channel")
    KStream<String, Order> homeDeliveryStream();
}
