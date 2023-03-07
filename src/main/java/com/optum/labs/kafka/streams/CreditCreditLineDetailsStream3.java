package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.Client;
import com.optum.labs.kafka.entity.CreditLines;
import com.optum.labs.kafka.entity.output.CreditLineUserDetailsOutput;
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

@Slf4j
@Service
public class CreditCreditLineDetailsStream3 {
    @Autowired
    private KStreamConfig kStreamConfig;

    public void creditLineDetails() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<CreditLines> creditLinesSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(CreditLines.class));
        KStream<String, CreditLines> creditLinesKStream = builder.stream(TopicEnum.CREDIT_LINE_DETAILS_1_TOPIC.getTopicName(), Consumed.with(Serdes.String(), creditLinesSerde));
        log.info("***** credit line stream data ******");
        creditLinesKStream.print(Printed.toSysOut());
        creditLinesKStream.foreach((key, value) ->
                log.info("***** key value for credit details: :{} :{}", key, value)
        );

        KStream<String, CreditLines> creditLinesInfoKeyStream = creditLinesKStream.selectKey((key, value) ->
                value.getId().toString()
        );

        final Serde<Client> clientDetailsSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Client.class));
        KStream<String, Client> clientDetailsKStream = builder.stream(TopicEnum.CLIENT_DETAILS_2_TOPIC.getTopicName(), Consumed.with(Serdes.String(), clientDetailsSerde));
        clientDetailsKStream.print(Printed.toSysOut());
        clientDetailsKStream.foreach(((key, value) ->
                log.info("***** key value for client details: :{} :{}", key, value)));
        KStream<String, Client> clientDetailsInfoKeyStream = clientDetailsKStream.selectKey((key, value) ->
                value.getId().toString()
        );

        ValueJoiner<CreditLines, Client, CreditLineUserDetailsOutput> joiner =
                (creditLine, client) ->
                        CreditLineUserDetailsOutput.builder()
                                .id(creditLine.getId())
                                .creditLineStatus(creditLine.getCreditLineStatus())
                                .postDt(creditLine.getPostDt())
                                .cif(creditLine.getCif())
                                .trans_crrncy_cd(creditLine.getTrans_crrncy_cd())
                                .flex_fee_debit_acc(creditLine.getFlex_fee_debit_acc())
                                .psgl_department(client.getPsgl_department())
                                .branchNbr(client.getBranchNbr())
                                .cba_aoteamcd(client.getCba_aoteamcd())
                                .nameAddRln2(client.getNameAddRln2())
                                .nameAddRln3(client.getNameAddRln3())
                                .nameAddRln4(client.getNameAddRln4())
                                .nameAddRln5(client.getNameAddRln5())
                                .nameAddRln6(client.getNameAddRln6())
                                .zipPostalCd(client.getZipPostalCd())
                                .fullName(client.getFullName())
                                .build();

        KStream<String, CreditLineUserDetailsOutput> creditLineUserDetailsOutputKStream = creditLinesInfoKeyStream
                .join(clientDetailsInfoKeyStream, joiner, JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), creditLinesSerde, clientDetailsSerde)
                );

        creditLineUserDetailsOutputKStream.print(Printed.toSysOut());
        creditLineUserDetailsOutputKStream.foreach(((key, value) ->
                log.info("***** key and value for creditLineUserDetailsOutput Stream:  *****: :{} :{}", key, value)
        ));

        creditLineUserDetailsOutputKStream.to(TopicEnum.CREDIT_LINE_DETAILS_TOPIC_OUTPUT_3_TOPIC.getTopicName(), Produced.with(Serdes.String(), new JsonSerde<>(CreditLineUserDetailsOutput.class)));
        kStreamConfig.topology(builder);
    }
}
