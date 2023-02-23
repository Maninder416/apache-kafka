package com.optum.labs.kafka;

import com.optum.labs.kafka.entity.BpaUlfProductCodes;
import com.optum.labs.kafka.entity.Instrument;
import com.optum.labs.kafka.entity.ProductCategory;
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

import java.time.Duration;
import java.util.Properties;

@SpringBootApplication
@Slf4j
public class SpringbootKafkaApplication implements CommandLineRunner {


    public static final String PRODUCT_DETAILS_TOPIC = "credit.creditlines-product-code.in";
    public static final String CATEGORY_DETAILS_TOPIC = "credit.creditlines.product-category.in";
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
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(BpaUlfProductCodes.class).getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Serdes.String().getClass());
        return props;
    }

    public void test() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Instrument> productCodeInfo = builder.stream(PRODUCT_DETAILS_TOPIC,Consumed.with(Serdes.String(),new JsonSerde<>(Instrument.class)));
        log.info("******** here instrument data is ********");
        productCodeInfo.print(Printed.toSysOut());
        productCodeInfo.foreach((key,value)->
                        log.info("product topic key value: "+key+" :value: "+value)
                );

        KStream<String, Instrument> productCodeInfoKeyStream = productCodeInfo.selectKey((key, value) ->
                value.getId().toString());

        productCodeInfoKeyStream.print(Printed.toSysOut());
        productCodeInfoKeyStream.foreach((key,value)->
                log.info("product code info key and value :{} :{}",key,value)
                );

        KStream<String, BpaUlfProductCodes> instrumentInfo = builder.stream(CATEGORY_DETAILS_TOPIC,Consumed.with(Serdes.String(),new JsonSerde<>(BpaUlfProductCodes.class)));
        log.info("******** here BpaUlfProductCodes data is ********");
        instrumentInfo.print(Printed.toSysOut());
        instrumentInfo.foreach((key,value)->
//                        log.info("category topic key value: "+key.toString()+ "value: "+value.toString())
                        log.info("category topic key: "+key+" and value: "+value.toString())
                );

        KStream<String, BpaUlfProductCodes> productCategoryInfoKeyStream = instrumentInfo.selectKey((key, value) ->
                value.getId().toString());
        productCategoryInfoKeyStream.print(Printed.toSysOut());
        productCategoryInfoKeyStream.foreach((key,value)->
            log.info("product category info key: "+key+": value: "+value.toString()));

        ValueJoiner<Instrument,BpaUlfProductCodes, ProductCategory> joiner=
                (instrument, bpaUldProductCodes)->
                        new ProductCategory.Builder()
                            .cif(instrument.getCif())
                            .product_category_cd(bpaUldProductCodes.getProduct_category_cd())
                            .accountNumber(instrument.getAccountNumber())
                            .applId(instrument.getApplId())
                            .build();


        KStream<String,ProductCategory> productCategoryInfoStream= productCodeInfoKeyStream
                .join(productCategoryInfoKeyStream,joiner, JoinWindows.of(Duration.ofSeconds(3000)));


        productCategoryInfoStream.print(Printed.toSysOut());
        productCategoryInfoStream.foreach((key,value)-> {
                    log.info("inside for loop");
                    log.info("product category join stream data key and value :{}, :{}", key, value.toString());
                }
                );

        productCategoryInfoStream.to(PRODUCT_CATEGORY_DETAILS_TOPIC,Produced.with(Serdes.String(),new JsonSerde<>(ProductCategory.class)));
        final Topology topology= builder.build();
        KafkaStreams kafkaStreams= new KafkaStreams(topology,properties());
        kafkaStreams.cleanUp();
        log.info("streaming started here ");
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
