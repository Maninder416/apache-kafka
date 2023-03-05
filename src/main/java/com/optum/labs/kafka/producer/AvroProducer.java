package com.optum.labs.kafka.producer;


import com.optum.labs.kafka.schema.StockHistory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Service
@Slf4j
public class AvroProducer {

    @Value("${avro.topic.name}")
    private String topicName;
    @Autowired
    private KafkaTemplate<String, StockHistory> kafkaTemplate;

    public void send(StockHistory stockHistory) {
        ListenableFuture<SendResult<String, StockHistory>> future =
                kafkaTemplate.send(topicName, String.valueOf(stockHistory.getTradeId()), stockHistory);
        future.addCallback(new ListenableFutureCallback<SendResult<String, StockHistory>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Message failed to produce");

            }

            @Override
            public void onSuccess(SendResult<String, StockHistory> result) {
                log.info("Avro message successfully send");
            }
        });

    }

}
