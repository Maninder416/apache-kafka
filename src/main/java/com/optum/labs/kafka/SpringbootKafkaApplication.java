package com.optum.labs.kafka;

import com.ibm.gbs.schema.Customer;
import com.ibm.gbs.schema.CustomerBalance;
import com.ibm.gbs.schema.Transaction;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Properties;

@SpringBootApplication
public class SpringbootKafkaApplication implements CommandLineRunner {

    public static final String CUSTOMER_DETAILS_TOPIC = "customer-details";
    public static final String TRANSACTION_DETAILS_TOPIC = "transaction-details";
    public static final String CUSTOMER_TRANSACTION_DETAILS_TOPIC = "customer-transaction-details";
    public static final String CUSTOMER_TRANSACTION_ENRICHMENT_APP = "customer-transaction-enrichment-app";
    public static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";
    public static final String KAFKA_BOOTSTRAP_SERVER = "localhost:9092";

    public static void main(String[] args) {
        SpringApplication.run(SpringbootKafkaApplication.class, args);
    }

    public Properties properties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, CUSTOMER_TRANSACTION_ENRICHMENT_APP);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        return props;
    }


    @Override
    public void run(String... args) throws Exception {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Customer> customerInfo = builder.stream(CUSTOMER_DETAILS_TOPIC); //  custom info data but it doesn't have any key defined
        customerInfo.print(Printed.toSysOut());
        // generating another stream with account id as key which is derived from customer info
        KStream<String, Customer> customerInfoKeyStream = customerInfo.selectKey((key, value) ->
                value.getAccountId().toString());

        customerInfoKeyStream.print(Printed.toSysOut());
        KStream<String, Transaction> accountBalInfo = builder.stream(TRANSACTION_DETAILS_TOPIC);
        accountBalInfo.print(Printed.toSysOut());

        KStream<String, Transaction> accountBalInfoKeyStream = accountBalInfo.selectKey((key, value) -> value.getAccountId().toString());
        accountBalInfoKeyStream.print(Printed.toSysOut());

        ValueJoiner<Transaction, Customer, CustomerBalance> joiner = (transaction, customer) ->
                CustomerBalance.newBuilder()
                        .setCustomerId(customer.getCustomerId())
                        .setBalance(transaction.getBalance())
                        .setAccountId(customer.getAccountId())
                        .setPhoneNumber(customer.getPhoneNumber())
                        .build();

        KStream<String, CustomerBalance> accountCustomerInfoStream = accountBalInfoKeyStream.join(
                customerInfoKeyStream,
                joiner,
                JoinWindows.of(Duration.ofSeconds(3000)));

        accountCustomerInfoStream.print(Printed.toSysOut());

        // stream of account balances

        accountCustomerInfoStream.to(CUSTOMER_TRANSACTION_DETAILS_TOPIC);
        final Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties());
        kafkaStreams.cleanUp();
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        // output stream where account is enriched with customer info


    }
}
