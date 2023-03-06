package com.optum.labs.kafka.consumer;

import com.optum.labs.kafka.model.StockHistoryEntity;
import com.optum.labs.kafka.repository.StockHistoryRepository;
import com.optum.labs.kafka.schema.StockHistory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AvroConsumerExample {
    @Autowired
    private StockHistoryRepository repository;

    @KafkaListener(topics = "${avro.topic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void read(ConsumerRecord<String, StockHistory> record) {
        String key = record.key();
        StockHistory history = record.value();
        StockHistoryEntity entity= new StockHistoryEntity();
        entity.setAmount(record.value().getAmount());
        entity.setPrice(record.value().getPrice());
        entity.setTradeId(record.value().getTradeId());
        entity.setTradeMarket(record.value().getTradeMarket().toString());
        entity.setStockName(record.value().getStockName().toString());
        entity.setTradeQuantity(record.value().getTradeQuantity());
        entity.setTradeType(record.value().getTradeType().toString());
        repository.save(entity);
        log.info("Key and value for avro received message is :{} :{} ", key, history.toString());

    }
}
