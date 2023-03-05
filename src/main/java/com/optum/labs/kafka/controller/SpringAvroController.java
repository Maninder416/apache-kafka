package com.optum.labs.kafka.controller;

import com.optum.labs.kafka.producer.AvroProducer;
import com.optum.labs.kafka.schema.StockHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class SpringAvroController {
    @Autowired
    private AvroProducer avroProducer;

    @PostMapping(value = "/sendStockHistory")
    public ResponseEntity<String> sendMessage(@RequestBody StockHistory stockHistory){
        log.info("Payload here is: "+stockHistory.toString());
        StockHistory history= StockHistory.newBuilder().build();
        history.setStockName(stockHistory.getStockName());
        history.setTradeType(stockHistory.getTradeType());
        history.setPrice(stockHistory.getPrice());
        history.setAmount(stockHistory.getAmount());
        history.setTradeId(new Random(1000).nextInt());
        history.setTradeMarket(stockHistory.getTradeMarket());
        history.setTradeQuantity(stockHistory.getTradeQuantity());
        avroProducer.send(history);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Avro message send");
    }


}
