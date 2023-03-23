package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.output.CreditLineActivityOutput14;
import com.optum.labs.kafka.entity.output.CreditLineFlexFeeOutput5;
import com.optum.labs.kafka.entity.output.CreditLineLoanTxnProd15;
import com.optum.labs.kafka.entity.output.CurrencyLoanProductCategoryCodeOutput;
import com.optum.labs.kafka.utils.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.common.utils.Java;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Slf4j
public class FlexCreditLineActivityOutputStream15 {
    @Autowired
    private KStreamConfig kStreamConfig;

    public void flexCreditLineActivityLoanTxnOutputStream() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<CreditLineActivityOutput14> creditLineActivityOutput14Serde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(CreditLineActivityOutput14.class));
        KStream<String, CreditLineActivityOutput14> creditLineActivityOutput14KStream =
                builder
                        .stream(TopicEnum.FLEX_CREDIT_LINE_AND_ACTIVITY_OUT_14_TOPIC
                                .getTopicName(), Consumed.with(Serdes.String(), creditLineActivityOutput14Serde));

        creditLineActivityOutput14KStream.print(Printed.toSysOut());
        creditLineActivityOutput14KStream.foreach((key, value) ->
                log.info("***** credit line activity output topic-14: ***** :{} :{}", key, value));

        KStream<String, CreditLineActivityOutput14> creditLineActivityOutput14KStreamInfo = creditLineActivityOutput14KStream
                .selectKey(((key, value) -> value.getId().toString()));

        final Serde<CurrencyLoanProductCategoryCodeOutput> creditLineFlexFeeOutput12Serde = Serdes.serdeFrom(new JsonSerializer<>(),
                new JsonDeserializer<>(CurrencyLoanProductCategoryCodeOutput.class));

        KStream<String, CurrencyLoanProductCategoryCodeOutput> creditLineFlexFeeOutput12KStream =
                builder.stream(TopicEnum.CURRENCY_LOAN_PRODUCT_CATEGORY_12_TOPIC.getTopicName(),
                        Consumed.with(Serdes.String(), creditLineFlexFeeOutput12Serde));

        creditLineFlexFeeOutput12KStream.print(Printed.toSysOut());
        creditLineFlexFeeOutput12KStream.foreach(((key, value) ->
                log.info("**** credit line output in stream 15 topic-12 *****: :{} :{}", key, value)));
        KStream<String, CurrencyLoanProductCategoryCodeOutput> currencyLoanProductCategoryCodeOutputKStreamInfo =
                creditLineFlexFeeOutput12KStream.selectKey((key, value) -> value.getId().toString());

        ValueJoiner<CreditLineActivityOutput14, CurrencyLoanProductCategoryCodeOutput, CreditLineLoanTxnProd15>
                joiner = (creditLineDetails, currencyCode) ->
                CreditLineLoanTxnProd15.builder()
                        .id(creditLineDetails.getId())
                        .customerLineNumber(creditLineDetails.getCustomerLineNumber())
                        .postDt(creditLineDetails.getPostDt())
                        .cif(creditLineDetails.getCif())
                        .effdt(creditLineDetails.getEffdt())
                        .flex_cmtmnt_amt_lcy(creditLineDetails.getFlex_cmtmnt_amt_lcy())
                        .flex_cmtmnt_amt_tcy(creditLineDetails.getFlex_cmtmnt_amt_tcy())
                        .flex_unCmtMnt_amt_lcy(creditLineDetails.getFlex_unCmtMnt_amt_lcy())
                        .flex_unCmtMnt_amt_tcy(creditLineDetails.getFlex_unCmtMnt_amt_tcy())
                        .flex_fee_pct(creditLineDetails.getFlex_fee_pct())
                        .flex_fee_accr_bas(creditLineDetails.getFlex_fee_accr_bas())
                        .trans_crrncy_cd(creditLineDetails.getTrans_crrncy_cd())
                        .entity(creditLineDetails.getEntity())
                        .applId(creditLineDetails.getApplId())
                        .src_updt_dt(creditLineDetails.getSrc_updt_dt())
                        .dw_create_ts(creditLineDetails.getDw_create_ts())
                        .created_by(creditLineDetails.getCreated_by())
                        .applId_loan(creditLineDetails.getApplId_loan())
                        .creditLineStatus(creditLineDetails.getCreditLineStatus())
                        .psgl_department(creditLineDetails.getPsgl_department())
                        .branchNbr(creditLineDetails.getBranchNbr())
                        .cba_aoteamcd(creditLineDetails.getCba_aoteamcd())
                        .nameAddRln1(creditLineDetails.getNameAddRln1())
                        .nameAddRln2(creditLineDetails.getNameAddRln2())
                        .nameAddRln3(creditLineDetails.getNameAddRln3())
                        .nameAddRln4(creditLineDetails.getNameAddRln4())
                        .nameAddRln5(creditLineDetails.getNameAddRln5())
                        .nameAddRln6(creditLineDetails.getNameAddRln6())
                        .zipPostalCd(creditLineDetails.getZipPostalCd())
                        .fullName(creditLineDetails.getFullName())
                        .statusCd(creditLineDetails.getStatusCd())
                        .expiryDate(creditLineDetails.getExpiryDate())
                        .acctNbr(currencyCode.getAcctNbr())
                        .tranId(currencyCode.getTranId())
                        .notePrncplBalgross(currencyCode.getNotePrncplBalgross())
                        .currencyCode(currencyCode.getCurrencyCode())
                        .product_cd(currencyCode.getProduct_cd())
                        .product_category_cd(currencyCode.getProduct_category_cd())
                        .build();

        KStream<String, CreditLineLoanTxnProd15> creditLineLoanTxnProd15KStream = creditLineActivityOutput14KStreamInfo
                .join(currencyLoanProductCategoryCodeOutputKStreamInfo, joiner,
                        JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), creditLineActivityOutput14Serde, creditLineFlexFeeOutput12Serde));

        creditLineLoanTxnProd15KStream.print(Printed.toSysOut());
        creditLineLoanTxnProd15KStream.foreach((key, value) ->
                log.info("**** final output of stream 15**** :{} :{}", key, value));

        final Serde<CreditLineLoanTxnProd15> creditLineLoanTxnProd15Serde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(CreditLineLoanTxnProd15.class));
        creditLineLoanTxnProd15KStream.to(TopicEnum.CREDIT_LINE_TOPIC_15.getTopicName(), Produced.with(Serdes.String(), new JsonSerde<>(CreditLineLoanTxnProd15.class)));

//        KTable<String, CreditLineLoanTxnProd15> kTable = builder.table(TopicEnum.CREDIT_LINE_TOPIC_15.getTopicName(),
//                Materialized.<String, CreditLineLoanTxnProd15, KeyValueStore<Bytes, byte[]>>as("ktable-store")
//                        .withKeySerde(Serdes.String())
//                        .withValueSerde(creditLineLoanTxnProd15Serde));
//
//
//        LocalDate start= LocalDate.of(2020,6,1);
//        LocalDate end= LocalDate.of(2022,6,1);
//
//
//        kTable.filter(
//                (key, value) ->{
//                    log.info("before change post date: "+value.getPostDt());
//                    LocalDate date= LocalDate.parse(value.getPostDt());
//                    log.info("post date is: "+date);
//                    return date!=null && date.isAfter(LocalDate.parse(value.getEffdt())) && date.isBefore(end);
//                }
//        ).toStream().to("demo2");

        kStreamConfig.topology(builder);
    }
}
