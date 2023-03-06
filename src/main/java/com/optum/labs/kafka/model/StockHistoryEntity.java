package com.optum.labs.kafka.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "STOCK_HISTORY")
@Data
public class StockHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer tradeId;
    private Integer tradeQuantity;
    private String tradeMarket;
    private String stockName;
    private String tradeType;
    private Float price;
    private double amount;

}
