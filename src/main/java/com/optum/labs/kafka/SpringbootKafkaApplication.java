package com.optum.labs.kafka;

import com.optum.labs.kafka.entity.BpaUlfProductCodes;
import com.optum.labs.kafka.entity.Instrument;
import com.optum.labs.kafka.service.DataGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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

import java.util.Properties;

@SpringBootApplication
@Slf4j
public class SpringbootKafkaApplication implements CommandLineRunner {


    public static final String PRODUCT_DETAILS_TOPIC = "credit.creditlines-product-code.in";
    public static final String CATEGORY_DETAILS_TOPIC = "product-category2.in";
    public static final String PRODUCT_CATEGORY_DETAILS_TOPIC = "credit.creditlines.product-category-product-code.out";
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
        log.info("******** Data is inserted into tables ********");
        log.info("******* Trying to send streaming data *******");
        test();
    }

    public Properties properties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, PRODUCT_CATEGORY_APP_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(Instrument.class).getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(BpaUlfProductCodes.class).getClass());

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Instrument.class);
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BpaUlfProductCodes.class);
        return props;
    }

    public void test() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Instrument> productCodeInfo = builder.stream(PRODUCT_DETAILS_TOPIC,Consumed.with(Serdes.String(),new JsonSerde<>(Instrument.class)));
        System.out.println("******** here instrument data is ********");
        productCodeInfo.print(Printed.toSysOut());
        productCodeInfo.foreach((key,value)->
                        System.out.println("product topic key value: "+key+" :value: "+value)
                );

        KStream<String, Instrument> productCodeInfoKeyStream = productCodeInfo.selectKey((key, value) ->
                value.getId().toString());

        productCodeInfoKeyStream.print(Printed.toSysOut());

        KStream<String, BpaUlfProductCodes> instrumentInfo = builder.stream(CATEGORY_DETAILS_TOPIC,Consumed.with(Serdes.String(),new JsonSerde<>(BpaUlfProductCodes.class)));
        System.out.println("******** here BpaUlfProductCodes data is ********");
        instrumentInfo.print(Printed.toSysOut());
        instrumentInfo.foreach((key,value)->
                        System.out.println("category topic key value: "+key.toString()+ "value: "+value.toString())
                );
//
//        KStream<String, Instrument> instrumentInfoKeyStream = instrumentInfo.selectKey((key, value) ->
//                value.getId().toString());
//        instrumentInfoKeyStream.print(Printed.toSysOut());
//        ValueJoiner<Instrument,BpaUlfProductCodes, ProductCategory> joiner=
//                (instrument, bpaUldProductCodes)->
//                        new ProductCategory.Builder()
//                                .cif(instrument.getCif())
//                                .product_category_cd(bpaUldProductCodes.getProduct_category_cd())
//                                .accountNumber(instrument.getAccountNumber())
//                                .applId(instrument.getApplId())
//                                .build();

//        KStream<String,ProductCategory> productCategoryInfoStream= instrumentInfoKeyStream
//                .join(productInfoKeyStream,joiner, JoinWindows.of(Duration.ofSeconds(3000)));

//        System.out.println("******** here productCategory data is ********");
//        productCategoryInfoStream.print(Printed.toSysOut());
//
//        productCategoryInfoStream.to(PRODUCT_CATEGORY_APP_ID);
        final Topology topology= builder.build();
        System.out.println("properties are: "+properties());
        KafkaStreams kafkaStreams= new KafkaStreams(topology,properties());
        kafkaStreams.cleanUp();
        System.out.println("streaming started here ");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
