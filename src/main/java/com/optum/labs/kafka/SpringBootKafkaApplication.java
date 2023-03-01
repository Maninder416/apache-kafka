package com.optum.labs.kafka;

import com.optum.labs.kafka.entity.*;
import com.optum.labs.kafka.entity.output.*;
import com.optum.labs.kafka.service.DataGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.Properties;

@SpringBootApplication
@Slf4j
public class SpringBootKafkaApplication implements CommandLineRunner {


    public static final String CREDIT_LINE_DETAILS_TOPIC = "credit.creditlines.creditline-detials.in";
    public static final String CLIENT_DETAILS_TOPIC = "credit.creditlines.ulf-client-detials.in";

    public static final String CREDIT_LINE_DETAILS_TOPIC_OUTPUT = "credit.creditlines.creditline-details.out";

    public static final String FLEX_CREDIT_LINE_ACTIVITY_INPUT = "credit.creditlines.flex-creditline-activity.in";
    public static final String PRODUCT_DETAILS_TOPIC = "credit.creditlines-product-code.in";
    public static final String CATEGORY_DETAILS_TOPIC = "credit.creditlines.product-category.in";
    public static final String PRODUCT_CATEGORY_DETAILS_TOPIC = "credit.creditlines.product-category-product-code.out";
    public static final String CURRENCY_CODE_TOPIC = "credit.creditlines.currency-code.in";
    public static final String LOAN_TXN_TOPIC = "credit.creditlines.loantxn.activity.in";
    public static final String CURRENCY_LOAN_TOPIC = "credit.creditlines.currency-code-loantxn.activity.out";
    public static final String CURRENCY_LOAN_PRODUCT_CATEGORY_TOPIC = "credit.creditlines.currency-loantxn-product-category-code.out";
    public static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
    public static final String KAFKA_BOOTSTRAP_SERVER = "localhost:9092";
    public static final String PRODUCT_CATEGORY_APP_ID = "customer-transaction-enrichment-app";

    @Autowired
    DataGenerationService dataCreationService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        dataCreationService.generateDataForbpaUlfProductCodes();
        dataCreationService.generateDataForClient();
        dataCreationService.generateDataForCreditLine();
        dataCreationService.generateDataForFlexFeeActivity();
        dataCreationService.generateDataForInstrument();
        dataCreationService.generateDataForLoan();
        dataCreationService.generateDataForpsRate();
        dataCreationService.generateDataFortestHolidayCalendar();
        dataCreationService.generateDataForTestLoanTransHist();
        dataCreationService.generateDataForFlexActivity();
        log.info("******** Data is inserted into tables ********");
        log.info("******* Trying to send streaming data *******");
//        productCategoryCodeStream();
        //  currencyCodeLoanTxnActivityStream();
        // creditLinesCurrencyLoanTxn12();
        //  creditLineDetails();
        flexCreditLineActivityStream();
    }

    public Properties properties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, PRODUCT_CATEGORY_APP_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        return props;
    }

    public void productCategoryCodeStream() {
        final Serde<Instrument> instrumentSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Instrument.class));
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Instrument> productCodeInfo = builder.stream(PRODUCT_DETAILS_TOPIC, Consumed.with(Serdes.String(), instrumentSerde));
        productCodeInfo.print(Printed.toSysOut());
        productCodeInfo.foreach((key, value) ->
                log.info("product topic key value: " + key + " :value: " + value)
        );

        KStream<String, Instrument> productCodeInfoKeyStream = productCodeInfo.selectKey((key, value) ->
                value.getId().toString());

        productCodeInfoKeyStream.print(Printed.toSysOut());
        productCodeInfoKeyStream.foreach((key, value) ->
                log.info("product code info key and value :{} :{}", key, value)
        );
        final Serde<BpaUlfProductCodes> bpaUlfProductCodesSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(BpaUlfProductCodes.class));
        KStream<String, BpaUlfProductCodes> instrumentInfo = builder.stream(CATEGORY_DETAILS_TOPIC, Consumed.with(Serdes.String(), bpaUlfProductCodesSerde));
        log.info("******** here BpaUlfProductCodes data is ********");
        instrumentInfo.print(Printed.toSysOut());
        instrumentInfo.foreach((key, value) ->
                log.info("category topic key: " + key + " and value: " + value.toString())
        );

        KStream<String, BpaUlfProductCodes> productCategoryInfoKeyStream = instrumentInfo.selectKey((key, value) ->
                value.getId().toString());
        productCategoryInfoKeyStream.print(Printed.toSysOut());
        productCategoryInfoKeyStream.foreach((key, value) ->
                log.info("product category info key: " + key + ": value: " + value.toString()));


        ValueJoiner<Instrument, BpaUlfProductCodes, ProductCategory> joiner =
                (instrument, bpaUldProductCodes) ->
                        new ProductCategory.Builder()
                                .id(instrument.getId())
                                .accountNumber(instrument.getAccountNumber())
//                                .applId(instrument.getApplId())
//                                .cif(instrument.getCif())
                                .product_category_cd(bpaUldProductCodes.getProduct_category_cd())
                                .product_cd(bpaUldProductCodes.getProduct_cd())
                                .build();

        KStream<String, ProductCategory> productCategoryInfoStream = productCodeInfoKeyStream
                .join(productCategoryInfoKeyStream, joiner,
                        JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), instrumentSerde, bpaUlfProductCodesSerde));

        productCategoryInfoStream.print(Printed.toSysOut());
        productCategoryInfoStream.foreach((key, value) -> {
                    log.info("product category join stream data key" + key + " and value: " + value.toString());
                }
        );
        productCategoryInfoStream.to(PRODUCT_CATEGORY_DETAILS_TOPIC, Produced.with(Serdes.String(), new JsonSerde<>(ProductCategory.class)));
        final Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());
        kafkaStreams.cleanUp();
        log.info("streaming started here ");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    public void currencyCodeLoanTxnActivityStream() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<Instrument> instrumentSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Instrument.class));
        KStream<String, Instrument> instrumentKStream = builder.stream(CURRENCY_CODE_TOPIC, Consumed.with(Serdes.String(), instrumentSerde));
        instrumentKStream.print(Printed.toSysOut());
        instrumentKStream.foreach((key, value) ->
                log.info("***** key value for instrument stream is :{} :{} : ", key, value)
        );

        KStream<String, Instrument> instrumentInfoKeyStream = instrumentKStream.selectKey((key, value) ->
                value.getId().toString()
        );

        final Serde<TestLoanTransHist> testLoanTransHistSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(TestLoanTransHist.class));
        KStream<String, TestLoanTransHist> testLoanTransHistKStream = builder.stream(LOAN_TXN_TOPIC, Consumed.with(Serdes.String(), testLoanTransHistSerde));
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

        insturmentTestLoanTransOutputKStream.to(CURRENCY_LOAN_TOPIC, Produced.with(Serdes.String(), new JsonSerde<>(CurrencyCodeLoanTxnActivityOutput.class)));
        final Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());
        kafkaStreams.cleanUp();
        log.info("***** Starting kafka stream *****");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

    }

    public void creditLinesCurrencyLoanTxn12() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<ProductCategory> productCategorySerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ProductCategory.class));

        KStream<String, ProductCategory> productCategoryKStream = builder.stream(PRODUCT_CATEGORY_DETAILS_TOPIC, Consumed.with(Serdes.String(), productCategorySerde));
        productCategoryKStream.print(Printed.toSysOut());
        productCategoryKStream.foreach((key, value) ->
                log.info("***** product category topic input data key value ******: :{} :{}  ", key, value)
        );
        KStream<String, ProductCategory> productCategoryInfoKeyStream = productCategoryKStream.selectKey((key, value) ->
                value.getAccountNumber().toString()
        );


        final Serde<CurrencyCodeLoanTxnActivityOutput> currencyCodeLoanTxnActivityOutputSerde =
                Serdes.serdeFrom(new JsonSerializer<>(),
                        new JsonDeserializer<>(CurrencyCodeLoanTxnActivityOutput.class));
        KStream<String, CurrencyCodeLoanTxnActivityOutput> currencyCodeLoanTxnActivityOutputKStream =
                builder.stream(CURRENCY_LOAN_TOPIC, Consumed.with(Serdes.String(),
                        currencyCodeLoanTxnActivityOutputSerde));
        currencyCodeLoanTxnActivityOutputKStream.print(Printed.toSysOut());
        currencyCodeLoanTxnActivityOutputKStream.foreach((key, value) ->
                log.info("***** key value for currency code loan output topic: ***** :{} :{}", key, value)
        );

        KStream<String, CurrencyCodeLoanTxnActivityOutput> currencyCodeLoanTxnActivityOutputInfoStream =
                currencyCodeLoanTxnActivityOutputKStream.selectKey((key, value) ->
                        value.getAcctNbr().toString()
                );

        currencyCodeLoanTxnActivityOutputInfoStream.foreach((key, value) ->
                log.info("***** currencyCodeLoanTxnActivityOutputInfoStream *****: :{} :{}", key, value)
        );

        ValueJoiner<ProductCategory, CurrencyCodeLoanTxnActivityOutput, CurrencyLoanProductCategoryCodeOutput> joiner =
                (productCategory, currencyCodeTxnActivityOutput) ->
                        CurrencyLoanProductCategoryCodeOutput.builder()
                                .currencyCode(currencyCodeTxnActivityOutput.getCurrencyCode())
                                .acctNbr(currencyCodeTxnActivityOutput.getAcctNbr())
                                .effectiveDt(currencyCodeTxnActivityOutput.getEffectiveDt())
                                .tranId(currencyCodeTxnActivityOutput.getTranId())
                                .notePrncplBalgross(currencyCodeTxnActivityOutput.getNotePrncplBalgross())
                                .id(currencyCodeTxnActivityOutput.getId())
                                .postDt(currencyCodeTxnActivityOutput.getPostDt())
                                .product_cd(productCategory.getProduct_cd())
                                .product_category_cd(productCategory.getProduct_category_cd())
                                .build();

        KStream<String, CurrencyLoanProductCategoryCodeOutput> currencyLoanProductCategoryCodeOutputKStream =
                productCategoryInfoKeyStream.join(currencyCodeLoanTxnActivityOutputInfoStream, joiner,
                        JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), productCategorySerde, currencyCodeLoanTxnActivityOutputSerde)
                );

        currencyLoanProductCategoryCodeOutputKStream.print(Printed.toSysOut());
        currencyLoanProductCategoryCodeOutputKStream.foreach(((key, value) ->
                log.info("****** currency code loan txn activity output ******:  :{} :{} ", key, value)
        ));

        currencyLoanProductCategoryCodeOutputKStream.to(CURRENCY_LOAN_PRODUCT_CATEGORY_TOPIC, Produced.with(Serdes.String(),
                new JsonSerde<>(CurrencyLoanProductCategoryCodeOutput.class)));

        final Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());
        kafkaStreams.cleanUp();
        log.info("***** Starting kafka stream *****");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

    }

    public void creditLineDetails() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<CreditLines> creditLinesSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(CreditLines.class));
        KStream<String, CreditLines> creditLinesKStream = builder.stream(CREDIT_LINE_DETAILS_TOPIC, Consumed.with(Serdes.String(), creditLinesSerde));
        log.info("***** credit line stream data ******");
        creditLinesKStream.print(Printed.toSysOut());
        creditLinesKStream.foreach((key, value) ->
                log.info("***** key value for credit details: :{} :{}", key, value)
        );

        KStream<String, CreditLines> creditLinesInfoKeyStream = creditLinesKStream.selectKey((key, value) ->
                value.getId().toString()
        );

        final Serde<Client> clientDetailsSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Client.class));
        KStream<String, Client> clientDetailsKStream = builder.stream(CLIENT_DETAILS_TOPIC, Consumed.with(Serdes.String(), clientDetailsSerde));
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
                                .custLineNbr(creditLine.getCustLineNbr())
                                .applId_loan(creditLine.getApplId_loan())
                                .creditLineStatus(creditLine.getCreditLineStatus())
                                .applId(creditLine.getApplId())
                                .postDt(creditLine.getPostDt())
                                .psgl_department(client.getPsgl_department())
                                .branchNbr(client.getBranchNbr())
                                .cba_aoteamcd(client.getCba_aoteamcd())
                                .nameAddRln1(client.getNameAddRln1())
                                .nameAddRln2(client.getNameAddRln2())
                                .nameAddRln3(client.getNameAddRln3())
                                .nameAddRln4(client.getNameAddRln4())
                                .nameAddRln5(client.getNameAddRln5())
                                .nameAddRln6(client.getNameAddRln6())
                                .zipPostalCd(client.getZipPostalCd())
                                .fullName(client.getFullName())
                                .statusCd(client.getStatusCd())
                                .expiryDate(client.getExpiryDate())
                                .cif(client.getCif())
                                .build();

        KStream<String, CreditLineUserDetailsOutput> creditLineUserDetailsOutputKStream = creditLinesInfoKeyStream
                .join(clientDetailsInfoKeyStream, joiner, JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), creditLinesSerde, clientDetailsSerde)
                );

        creditLineUserDetailsOutputKStream.print(Printed.toSysOut());
        creditLineUserDetailsOutputKStream.foreach(((key, value) ->
                log.info("***** key and value for creditLineUserDetailsOutput Stream:  *****: :{} :{}", key, value)
        ));

        creditLineUserDetailsOutputKStream.to(CREDIT_LINE_DETAILS_TOPIC_OUTPUT, Produced.with(Serdes.String(), new JsonSerde<>(CreditLineUserDetailsOutput.class)));

        final Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());
        kafkaStreams.cleanUp();
        log.info("***** Starting kafka stream *****");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    public void flexCreditLineActivityStream() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<FlexFeeActivity> flexCreditLineActivityOutputSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(FlexFeeActivity.class));
        KStream<String, FlexFeeActivity> flexCreditLineActivityOutputKStream = builder.stream(FLEX_CREDIT_LINE_ACTIVITY_INPUT
                , Consumed.with(Serdes.String(), flexCreditLineActivityOutputSerde));
        flexCreditLineActivityOutputKStream.print(Printed.toSysOut());
        flexCreditLineActivityOutputKStream.foreach((key, value) ->
                log.info("***** key and value for flex activity :{} :{}", key, value));
        KStream<String, FlexFeeActivity> flexCreditLineActivityOutputInfo = flexCreditLineActivityOutputKStream
                .selectKey((key, value) ->
                        value.getId().toString());

        //creating the serdes to receive data from topic3
        final Serde<CreditLineUserDetailsOutput> creditLineUserDetailsOutputSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(CreditLineUserDetailsOutput.class));

        KStream<String, CreditLineUserDetailsOutput> creditLineUserDetailsOutputKStream =
                builder.stream(CREDIT_LINE_DETAILS_TOPIC_OUTPUT, Consumed.with(Serdes.String(), creditLineUserDetailsOutputSerde));
        creditLineUserDetailsOutputKStream.print(Printed.toSysOut());
        creditLineUserDetailsOutputKStream.foreach((key, value) ->
                log.info("***** credit line user details output: ****** :{} :{}", key, value)
        );
        KStream<String, CreditLineUserDetailsOutput> creditLineUserDetailsOutputInfo =
                creditLineUserDetailsOutputKStream.selectKey((key, value) ->
                        value.getId().toString()
                );

        ValueJoiner<FlexFeeActivity, CreditLineUserDetailsOutput, CreditLineFlexFeeOutput5> joiner =
                (flexFeeActivity, creditLineDetails) -> CreditLineFlexFeeOutput5.builder()
                        .id(flexFeeActivity.getId())
                        .effdt(flexFeeActivity.getEffdt())
                        .created_by(flexFeeActivity.getCreated_by())
                        .custLnNbr(flexFeeActivity.getCustLnNbr())
                        .dw_create_ts(flexFeeActivity.getDw_create_ts())
                        .flex_fee_pct(flexFeeActivity.getFlex_fee_pct())
                        .flex_fee_accr_bas(flexFeeActivity.getFlex_fee_accr_bas())
                        .flex_unCmtMnt_amt_lcy(flexFeeActivity.getFlex_unCmtMnt_amt_lcy())
                        .flex_unCmtMnt_amt_tcy(flexFeeActivity.getFlex_unCmtMnt_amt_tcy())
                        .flex_cmtmnt_amt_tcy(flexFeeActivity.getFlex_cmtmnt_amt_tcy())
                        .flex_cmtmnt_amt_lcy(flexFeeActivity.getFlex_cmtmnt_amt_lcy())
                        .trans_crrncy_cd(flexFeeActivity.getTrans_crrncy_cd())
                        .entity(flexFeeActivity.getEntity())
                        .src_updt_dt(flexFeeActivity.getSrc_updt_dt())
                        .custLineNbr(creditLineDetails.getCustLineNbr())
                        .applId_loan(creditLineDetails.getApplId_loan())
                        .creditLineStatus(creditLineDetails.getCreditLineStatus())
                        .applId(creditLineDetails.getApplId())
                        .postDt(creditLineDetails.getPostDt())
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
                        .cif(creditLineDetails.getCif())

                        .build();

        KStream<String, CreditLineFlexFeeOutput5> creditLineFlexFeeOutput5KStream =
                flexCreditLineActivityOutputInfo.join(creditLineUserDetailsOutputInfo, joiner,
                        JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), flexCreditLineActivityOutputSerde, creditLineUserDetailsOutputSerde)
                );
        creditLineFlexFeeOutput5KStream.print(Printed.toSysOut());
        creditLineFlexFeeOutput5KStream.foreach(((key, value) ->
                log.info("***** key and value for combined creditLineFlexFeeOutput5KStream ******: :{} :{}", key, value)
        ));

        creditLineFlexFeeOutput5KStream.to("testing2",Produced.with(Serdes.String(),new JsonSerde<>(CreditLineFlexFeeOutput5.class)));


        final Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());
        kafkaStreams.cleanUp();
        log.info("***** Starting kafka stream *****");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
