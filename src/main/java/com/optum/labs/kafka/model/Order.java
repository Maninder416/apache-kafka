package com.optum.labs.kafka.model;

import lombok.Data;

@Data
public class Order {

    private String item;
    private int quantity;
    private String deliveryType;


}
