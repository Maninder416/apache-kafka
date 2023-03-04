package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.Instrument;
import com.optum.labs.kafka.entity.TestLoanTransHist;
import com.optum.labs.kafka.entity.output.CurrencyCodeLoanTxnActivityOutput;
import com.optum.labs.kafka.utils.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class CurrencyCodeLoanTxnStream8 {
    @Autowired
    private KStreamConfig kStreamConfig;

    /**
     * Creating the stream for topic 9 and 10 according to the diagram
     * topic numbers are mentioned in the screenshot
     */
    public void currencyCodeLoanTxnActivityStream() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<Instrument> instrumentSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Instrument.class));
        KStream<String, Instrument> instrumentKStream = builder.stream(TopicEnum.CURRENCY_CODE_6_TOPIC.getTopicName(), Consumed.with(Serdes.String(), instrumentSerde));
        instrumentKStream.print(Printed.toSysOut());
        instrumentKStream.foreach((key, value) ->
                log.info("***** key value for instrument stream is :{} :{} : ", key, value)
        );

        KStream<String, Instrument> instrumentInfoKeyStream = instrumentKStream.selectKey((key, value) ->
                value.getId().toString()
        );

        final Serde<TestLoanTransHist> testLoanTransHistSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(TestLoanTransHist.class));
        KStream<String, TestLoanTransHist> testLoanTransHistKStream = builder.stream(TopicEnum.LOAN_TXN_7_TOPIC.getTopicName(), Consumed.with(Serdes.String(), testLoanTransHistSerde));
        testLoanTransHistKStream.foreach((key, value) ->
                log.info("Key value for TestLoanTransHist stream: :{} :{} ", key, value)
        );

        KStream<String, TestLoanTransHist> testLoanTransHistInfoKeyStream = testLoanTransHistKStream.selectKey((key, value) ->
                value.getId().toString()
        );

        ValueJoiner<Instrument, TestLoanTransHist, CurrencyCodeLoanTxnActivityOutput> joiner =
                (instrument, testLoanTransHist) ->
                        new CurrencyCodeLoanTxnActivityOutput.Builder()
                                .setCurrencyCode(instrument.getCurrencyCode())
                                .setAcctNbr(testLoanTransHist.getAcctNbr())
                                .setEffectiveDt(testLoanTransHist.getEffectiveDt())
                                .setPostDt(testLoanTransHist.getPostDt())
                                .setId(testLoanTransHist.getId())
                                .setTranId(testLoanTransHist.getTranId())
                                .setNotePrncplBalgross(testLoanTransHist.getNotePrncplBalgross())
                                .build();

        KStream<String, CurrencyCodeLoanTxnActivityOutput> insturmentTestLoanTransOutputKStream = instrumentInfoKeyStream.
                join(testLoanTransHistInfoKeyStream, joiner, JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), instrumentSerde, testLoanTransHistSerde));

        insturmentTestLoanTransOutputKStream.print(Printed.toSysOut());
        insturmentTestLoanTransOutputKStream.foreach(((key, value) ->
                log.info("***** Instrument and test loan transHist join data is ****** :{} :{}", key, value.toString())
        ));

        insturmentTestLoanTransOutputKStream.to(TopicEnum.CURRENCY_LOAN_TOPIC.getTopicName(), Produced.with(Serdes.String(), new JsonSerde<>(CurrencyCodeLoanTxnActivityOutput.class)));
        kStreamConfig.topology(builder);

    }
}
