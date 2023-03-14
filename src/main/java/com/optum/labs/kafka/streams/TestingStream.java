package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.CanDelete;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TestingStream {

    @Autowired
    private KStreamConfig kStreamConfig;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = LocalDate.of(2022, 1, 15);

    int initial = 0;
    Double balance = 0.0;

    List<Double> doubleList = new ArrayList<>();

    List<CanDelete> canDeleteList = new ArrayList<>();

    List<CanDelete> list2 = new ArrayList<>();

    List<CanDelete> outputList = new ArrayList<>();

    List<CanDelete> newList = new ArrayList<>();


    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
        log.info("**** start date: *****: " + startDate);
        log.info("**** end date *****: " + endDate);
        StreamsBuilder builder = new StreamsBuilder();

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
                .limit(days)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());


        for (int i = 0; i < dates.size(); i++) {
            CanDelete cd = new CanDelete();
            cd.setId((long) i);
            cd.setPostDate(dates.get(i).toString());
            cd.setEffectiveDate(dates.get(i).toString());
            cd.setAmount(0.0);
            cd.setAccountBalance(0.0);
            list2.add(cd);
        }

        final Serde<String> keySerde = Serdes.String();
        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));

        // Print the List
        final double total = 0;

        test.foreach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + value);
            value.setAccountBalance(value.getAccountBalance() + value.getAmount());

        });

        test.mapValues(value -> {
            CanDelete obj = new CanDelete();
            obj.setId(value.getId());
            obj.setPostDate(value.getPostDate());
            obj.setEffectiveDate(value.getEffectiveDate());
            obj.setAmount(value.getAmount());
            getObject(obj);
            return value;
        });
//        }).to("test5", Produced.with(keySerde, canDeleteSerde));
        kStreamConfig.topology(builder);
        return null;

    }

    public List<CanDelete> getObject(CanDelete canDelete) {
        list2.add(canDelete);
        Map<Long, CanDelete> temp = list2.stream()
                .collect(Collectors.toMap(
                        CanDelete::getId,
                        Function.identity(),
                        (object1, object2) -> object1
                ));

        for (CanDelete object : list2) {
            if (object.getAmount() > 0.0) {
                temp.merge(object.getId(), object, (oldObject, newObject) -> newObject);
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        balance = setBalance(canDelete);
        decimalFormat.format(balance);
        System.out.println("balance amount is: " + balance);
        outputList.addAll(temp.values());
        for (CanDelete canDelete1 : outputList) {
            System.out.println("data details: " + canDelete1);
            if (canDelete1.getAmount() != 0.0) {
                canDelete1.setAccountBalance(Double.parseDouble(decimalFormat.format(balance)));
            }
            kafkaTemplate.send("topic5", canDelete1);
        }
        return outputList;
    }


    public double setBalance(CanDelete canDelete) {
        doubleList.add(canDelete.getAmount());
        double test = doubleList.stream().reduce(0.0, (a, b) -> a + b);
        System.out.println("print list here");
        doubleList.forEach(System.out::println);
        System.out.println("My final balance: " + balance);
        System.out.println("list size is: " + doubleList.size());
        System.out.println("total amount: " + test);
        return test;
    }


//    public void test() {
//        StreamsBuilder builder = new StreamsBuilder();
//
//        final Serde<String> keySerde = Serdes.String();
//        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
//
//        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
//        test.print(Printed.toSysOut());
//        test.foreach((key, value) ->
//                log.info("***** test topic data : ***** :{} :{}", key, value));


//        KStream<String, CanDelete> outputStream = test.mapValues(value -> {
//            LocalDate date = LocalDate.parse(value.getPostDate());
//            if (date.isBefore(end) && date.isAfter(start)) {
//                     value.setAccountBalance(value.getAmount());
//            }
//            return value;
//        });

//        test.mapValues(value -> {
//            LocalDate date = LocalDate.parse(value.getPostDate());
//            if (date.isBefore(end) && date.isAfter(start)) {
//                if (value.getAmount() != 0.0) {
//                    double amountBalance = value.getAmount();
//                    double accountBalance = value.getAccountBalance();
//                    accountBalance = accountBalance + amountBalance;
//                    value.setAccountBalance(accountBalance);
//                } else if (value.getAmount() == 0.0) {
//
//                }
//                value.setAccountBalance(value.getAmount());
//                log.info("amount is: " + value.getAmount());
//                log.info("account balance: " + value.getAccountBalance());
//            }
//
//            return value;
//        }).to("test5", Produced.with(keySerde, canDeleteSerde));
//        ;


    //test.to("test4", Produced.with(keySerde, canDeleteSerde));

//        KTable<String, CanDelete> balanceTable = builder.table("test4", Consumed.with(keySerde, canDeleteSerde));

//        balanceTable.filter((key, value) -> {
//            double balance = 0.00;
//            if(value.getBalance()==balance){
//
//            }
//
//
//        }).toStream().to("deed");

//        KTable<String,CanDelete> updatedBalanceTable= test
//                .groupBy((key,value)->key.substring(0,2))
//                .aggregate(
//                        ()->null,
//                        (key,value,aggregate)->value,
//                        Materialized.with(keySerde,canDeleteSerde)
//                ).leftJoin(
//                        balanceTable,
//                        (transactionAmount, balance)-> {
//                            if(balance == null){
//                                return transactionAmount;
//                            }else {
//                                return balance.getBalance()+transactionAmount.getBalance();
//                            }
//                        }
//
//                );

//        updatedBalanceTable.toStream().to("updated-balance",Produced.with(keySerde,canDeleteSerde));


//        KTable<String, CanDelete> table = builder.table(
//                "test3",
//                Materialized.<String, CanDelete, KeyValueStore<Bytes, byte[]>>as("ktable-store4")
//                        .withKeySerde(keySerde)
//                        .withValueSerde(canDeleteSerde)
//        );

//        KTable<String, CanDelete> table2= builder.table("test3");
//        table2.toStream().foreach((key,value)->{
//            System.out.println("key for table: "+key+" value: "+value);
//        });
//
//
//        kStreamConfig.topology(builder);
//    }

//    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
//        log.info("**** start date: *****: " + startDate);
//        log.info("**** end date *****: " + endDate);
//        StreamsBuilder builder = new StreamsBuilder();
//        final Serde<String> keySerde = Serdes.String();
//        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
//        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
//
//        List<LocalDate> dates = YearMonth.from(startDate).atDay(1)
//                .datesUntil(YearMonth.from(endDate).plusMonths(1).atDay(1))
//                .collect(Collectors.toList());
//        for (int i = 0; i < dates.size(); i++) {
//            System.out.println("dates are: " + dates.get(i));
//        }
//
//        List<CanDelete> canDeleteList= new ArrayList<>();
//
//        test.mapValues(value -> {
//            LocalDate date = LocalDate.parse(value.getPostDate());
//            System.out.println("date is: " + date);
//
//
//
//            if (date.isBefore(endDate) && date.isAfter(startDate)) {
//                for (int i = 0; i < dates.size(); i++) {
//
//
//                    if (!(dates.get(i).toString().equalsIgnoreCase(startDate.toString()) &&
//                            dates.get(i).toString().equalsIgnoreCase(endDate.toString()))) {
//                        value.setPostDate(dates.get(i).toString());
//                        value.setEffectiveDate(dates.get(i).toString());
//                        value.setAmount(0.0);
//                        value.setAccountBalance(0.0);
//                        canDeleteList.add(value);
//                    }
//                    else if (dates.get(i).toString().equalsIgnoreCase(startDate.toString()) &&
//                            dates.get(i).toString().equalsIgnoreCase(endDate.toString())) {
//
//                        if (value.getAmount() != 0.0) {
//                            double amountBalance = value.getAmount();
//                            double accountBalance = value.getAccountBalance();
//                            accountBalance = accountBalance + amountBalance;
//                            value.setAccountBalance(accountBalance);
//                            canDeleteList.add(value);
//                        }
//                    }
//                    value.setAccountBalance(value.getAmount());
//                    log.info("amount is: " + value.getAmount());
//                    log.info("account balance: " + value.getAccountBalance());
//
//                }
//            }
//            System.out.println("size of list: "+canDeleteList.size());
//            for(int m=0;m<canDeleteList.size();m++){
//                System.out.println("values are: "+canDeleteList.get(m));
//
//            }
//            return value;
//
//
//        }).to("test5", Produced.with(keySerde, canDeleteSerde));
//
//
//
//        kStreamConfig.topology(builder);
//        return null;
//
//    }

    public List<CanDelete> getDataBetweenDates1(LocalDate startDate, LocalDate endDate) {
        log.info("**** start date: *****: " + startDate);
        log.info("**** end date *****: " + endDate);
        StreamsBuilder builder = new StreamsBuilder();

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
                .limit(days)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());

        List<CanDelete> canDeleteList = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            CanDelete cd = new CanDelete();
            cd.setId((long) i);
            cd.setPostDate(dates.get(i).toString());
            cd.setEffectiveDate(dates.get(i).toString());
            cd.setAmount(0.0);
            cd.setAccountBalance(0.0);
            canDeleteList.add(cd);
        }

        final Serde<String> keySerde = Serdes.String();
        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));

        test.mapValues(value -> {
            balance = value.getAmount();
            log.info("value called");
            for (CanDelete delete : canDeleteList) {
                if (delete.getId().equals(value.getId())) {
                    log.info("value id: " + value.getId());
                    if (value.getAmount() != 0.0) {
                        // log.info("value amount: "+value.getAmount());
                        delete.setAmount(value.getAmount());
                        System.out.println("AMOUNT: " + value.getAmount());
                        // log.info("get amount: "+delete.getAmount());
//                        balance = delete.getAmount();
                        System.out.println("BALANCE AMOUNT: " + balance);
                        //   log.info("balance amount is: ",balance);
                        delete.setAccountBalance(balance);
                    }
                }
            }

            // temp(canDeleteList);

            for (CanDelete canDelete : canDeleteList) {
                log.info("data for this list is: " + canDelete);
            }
//            log.info("list of data size: ", canDeleteList.size());

//            test2(canDeleteList);
            // balance = 0.0;
//            for (int m = 0; m < canDeleteList.size(); m++) {
//                if (canDeleteList.get(m).getAmount() != 0.0) {
//                    balance += canDeleteList.get(m).getAmount();
//                    canDeleteList.get(m).setAccountBalance(balance);
//                }
////                kafkaTemplate.send("test5", canDeleteList.get(m));
//            }
//            for (CanDelete canDelete : canDeleteList) {
//                log.info("data is: " + canDelete);
////                kafkaTemplate.send("test5", canDelete);
//            }
            return value;
        });

        kStreamConfig.topology(builder);
        System.out.println("list is: " + canDeleteList);
        canDeleteList.forEach(System.out::println);
        System.out.println("====== complete");

        return null;

    }

//    public void test2(List<CanDelete> canDeleteList) {
//
//        if (initial == 0) {
//            System.out.println("initial value is: "+initial);
//            for (int m = 0; m < canDeleteList.size(); m++) {
//                if (canDeleteList.get(m).getAmount() != 0) {
//                    balance += canDeleteList.get(m).getAmount();
//                    canDeleteList.get(m).setAccountBalance(balance);
//                }
//                log.info("data: "+canDeleteList.get(m));
//            }
//
//            initial++;
//        }
//    }

    public void temp(List<CanDelete> canDeleteList) {
        log.info("initial value " + initial);
        if (initial == 0) {
            for (CanDelete canDelete : canDeleteList) {
                log.info("data for this list is: " + canDelete);
            }
        }
        initial++;
    }


}
