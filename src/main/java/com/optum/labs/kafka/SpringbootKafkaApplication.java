package com.optum.labs.kafka;

import com.optum.labs.kafka.entity.*;
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
public class SpringbootKafkaApplication implements CommandLineRunner {


    public static final String PRODUCT_DETAILS_TOPIC = "credit.creditlines-product-code.in";
    public static final String CATEGORY_DETAILS_TOPIC = "credit.creditlines.product-category.in";
    public static final String PRODUCT_CATEGORY_DETAILS_TOPIC = "credit.creditlines.product-category-product-code.out";
    public static final String CURRENCY_CODE_TOPIC= "credit.creditlines.currency-code.in";
    public static final String LOAN_TXN_TOPIC= "credit.creditlines.loantxn.activity.in";
    public static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
    public static final String KAFKA_BOOTSTRAP_SERVER = "localhost:9092";
    public static final String PRODUCT_CATEGORY_APP_ID = "customer-transaction-enrichment-app";

    @Autowired
    DataGenerationService dataCreationService;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootKafkaApplication.class, args);
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
       // productCategoryCodeStream();
        currencyCodeLoanTxnActivityStream();
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
                                .applId(instrument.getApplId())
                                .cif(instrument.getCif())
                                .product_category_cd(bpaUldProductCodes.getProduct_category_cd())
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

    public void currencyCodeLoanTxnActivityStream(){
        StreamsBuilder builder= new StreamsBuilder();
//        final Serde<FlexActivity> flexActivitySerde= Serdes.serdeFrom(new JsonSerializer<>(),new JsonDeserializer<>(FlexActivity.class));
//        KStream<String,FlexActivity> flexActivityKStream= builder.stream(CURRENCY_CODE_TOPIC,Consumed.with(Serdes.String(),flexActivitySerde));
//        flexActivityKStream.print(Printed.toSysOut());
//        flexActivityKStream.foreach((key,value)->
//                log.info("***** key :{} value :{} for flex activity stream is: ",key,value)
//                );
//
//        KStream<String,FlexActivity> flexActivityInfoKeyStream= flexActivityKStream.selectKey((key,value)->
//                value.getId().toString()
//                );

        final Serde<Client> clientSerde= Serdes.serdeFrom(new JsonSerializer<>(),new JsonDeserializer<>(Client.class));
        KStream<String,Client> clientKStream= builder.stream(LOAN_TXN_TOPIC,Consumed.with(Serdes.String(),clientSerde));
        clientKStream.foreach((key,value)->
                log.info("Key :{} value :{} for client stream: ",key,value)
                );

        KStream<String,Client> clientKStreamInfo = clientKStream.selectKey((key,value)->
                value.getId().toString()
                );



        final Topology topology= builder.build();
        KafkaStreams kafkaStreams= new KafkaStreams(topology,properties());
        kafkaStreams.cleanUp();
        log.info("***** Starting kafka stream *****");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));


    }
}
