package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.CanDelete;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TestingStream {

    @Autowired
    private KStreamConfig kStreamConfig;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = LocalDate.of(2022, 1, 15);

    int initial = 0;
    Double balance = 0.0;

    boolean value= true;

    int count=0;

    int tmp=0;

    List<Double> doubleList = new ArrayList<>();

    List<CanDelete> canDeleteList = new ArrayList<>();

    List<CanDelete> list2 = new ArrayList<>();
    List<CanDelete> outputList = new ArrayList<>();
    List<CanDelete> newList = new ArrayList<>();

    Set<CanDelete> setValues = new HashSet<>();

    List<CanDelete> oldList= new ArrayList<>();

    int count2 = 0;

    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
        Set<CanDelete> canDeletes = new HashSet<>();
        log.info("**** start date: *****: " + startDate);
        log.info("**** end date *****: " + endDate);
        StreamsBuilder builder = new StreamsBuilder();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
                .limit(days)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
        System.out.println("date list size: " + dates.size());

        for (int i = 0; i < dates.size(); i++) {
            CanDelete cd = new CanDelete();
            cd.setId((long) i + 5);
            cd.setPostDate(dates.get(i).toString());
            cd.setEffectiveDate(dates.get(i).toString());
            cd.setAmount(0.0);
            cd.setAccountBalance(0.0);

            list2.add(cd);
        }

        final Serde<String> keySerde = Serdes.String();
        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
        test.filter(new Predicate<String, CanDelete>() {
            @Override
            public boolean test(String key, CanDelete value) {
                balance += value.getAmount();
                value.setAccountBalance(balance);
                for (int i = 0; i < list2.size(); i++) {
                    if (list2.get(i).getPostDate().equalsIgnoreCase(value.getPostDate()) && value.getAmount() > 0.0) {
                        list2.remove(i);
                        list2.add(value);
                        System.out.println("================================================");
                    }
                    if (i == list2.size() - 1) {
                        System.out.println("run time: ");
                    }
                }
                list2.forEach(a -> System.out.println("here new list is: " + a));
                return true;
            }
        });

        kStreamConfig.topology(builder);
        return null;
    }



    @Scheduled(fixedDelay = 60000)
    public void setUpdatedData() {
        count2 = restTemplate.getForObject("http://localhost:9001/count", Integer.class);
        if(count2!=0) {
            for (CanDelete canDelete : list2) {
                System.out.println("****** sending messages *******");
                    kafkaTemplate.send("topic10", canDelete);
            }
        }
        System.out.println("list outside method: " + list2);
    }

    public List<CanDelete> getAllListElement(){
        return list2;
    }

    public Integer consumerCode(){
        KafkaConsumer<String,CanDelete> consumer= new KafkaConsumer<String, CanDelete>(kStreamConfig.properties());
        consumer.subscribe(Collections.singleton("test3"));

        try{
            while (true){
                ConsumerRecords<String,CanDelete> records= consumer.poll(Duration.ofMillis(100));
                if(records.count()==0) break;
                count2+=records.count();
                System.out.println("Count value is: "+count2);
            }
        }catch (Exception e){

        }finally {
            consumer.close();
        }
        return count2;
    }



//    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
//        Set<CanDelete> canDeletes = new HashSet<>();
//        log.info("**** start date: *****: " + startDate);
//        log.info("**** end date *****: " + endDate);
//        StreamsBuilder builder = new StreamsBuilder();
//
//        long days = ChronoUnit.DAYS.between(startDate, endDate);
//        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
//                .limit(days)
//                .mapToObj(i -> startDate.plusDays(i))
//                .collect(Collectors.toList());
//        System.out.println("date list size: " + dates.size());
//
//        for (int i = 0; i < dates.size(); i++) {
//            CanDelete cd = new CanDelete();
//            cd.setId((long) i + 5);
//            cd.setPostDate(dates.get(i).toString());
//            cd.setEffectiveDate(dates.get(i).toString());
//            cd.setAmount(0.0);
//            cd.setAccountBalance(0.0);
//
//            list2.add(cd);
//        }
//
//        final Serde<String> keySerde = Serdes.String();
//        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
//        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
//
//        test.foreach((key,value)->{
//
//        });
//
//        test.filter(new Predicate<String, CanDelete>() {
//            @Override
//            public boolean test(String key, CanDelete value) {
//                balance += value.getAmount();
//                value.setAccountBalance(balance);
//                for (int i = 0; i < list2.size(); i++) {
//                    if (list2.get(i).getPostDate().equalsIgnoreCase(value.getPostDate()) && value.getAmount() > 0.0) {
//                        list2.remove(i);
//                        list2.add(value);
//                        System.out.println("================================================");
//                    }
//                    if(i==list2.size()-1){
//                        System.out.println("run time: ");
//                    }
//                }
//                list2.forEach(a-> System.out.println("here new list is: "+a));
//                return true;
//            }
//        });
//
//
//        System.out.println("output list:" + outputList.size());
//        kStreamConfig.topology(builder);
//
//        return null;
//    }


//    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
//        Set<CanDelete> canDeletes = new HashSet<>();
//        log.info("**** start date: *****: " + startDate);
//        log.info("**** end date *****: " + endDate);
//        StreamsBuilder builder = new StreamsBuilder();
//
//        long days = ChronoUnit.DAYS.between(startDate, endDate);
//        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
//                .limit(days)
//                .mapToObj(i -> startDate.plusDays(i))
//                .collect(Collectors.toList());
//        System.out.println("date list size: " + dates.size());
//
//        for (int i = 0; i < dates.size(); i++) {
//            CanDelete cd = new CanDelete();
//            cd.setId((long) i + 5);
//            cd.setPostDate(dates.get(i).toString());
//            cd.setEffectiveDate(dates.get(i).toString());
//            cd.setAmount(0.0);
//            cd.setAccountBalance(0.0);
//
//            list2.add(cd);
//        }
//
//        final Serde<String> keySerde = Serdes.String();
//        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
//        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
//
//
//        KStream<String, Double> input = builder.stream("test3");
//
//// Group the records by key and count the number of records
//        KTable<String, Long> countTable = input.groupByKey().count();
//
//// Convert the KTable to a stream and print the count to the console
//        countTable.toStream().foreach((key, value) -> System.out.println("Count of messages with key " + key + " is " + value));
//
//
//
//
//        test.filter(new Predicate<String, CanDelete>() {
//            @Override
//            public boolean test(String key, CanDelete value) {
//                balance += value.getAmount();
//                value.setAccountBalance(balance);
//                for (int i = 0; i < list2.size(); i++) {
//                    if (list2.get(i).getPostDate().equalsIgnoreCase(value.getPostDate()) && value.getAmount() > 0.0) {
//                        list2.remove(i);
//                        list2.add(value);
//                        System.out.println("================================================");
//                    }
//                }
//                list2.forEach(a-> System.out.println("here new list is: "+a));
//                return true;
//            }
//        });
//
//        list2.forEach(a-> System.out.println("List data outside is: "+a));
//
//        System.out.println("output list:" + outputList.size());
//        kStreamConfig.topology(builder);
//
////        list2.forEach(a-> System.out.println("outside nisag: "+a));
//        return null;
//    }

    public void testing(List<CanDelete> tmp) {
//        System.out.println("tmp value ");
        tmp.forEach(a -> System.out.println("tmp is: " + a));
    }

    public void testing1(CanDelete canDelete) {
        System.out.println("printing");
        System.out.println(list2.add(canDelete));
    }

    public void printList() {
        System.out.println(list2);
    }


//    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
//        Set<CanDelete> canDeletes = new HashSet<>();
//        log.info("**** start date: *****: " + startDate);
//        log.info("**** end date *****: " + endDate);
//        StreamsBuilder builder = new StreamsBuilder();
//
//        long days = ChronoUnit.DAYS.between(startDate, endDate);
//        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
//                .limit(days)
//                .mapToObj(i -> startDate.plusDays(i))
//                .collect(Collectors.toList());
//        System.out.println("date list size: " + dates.size());
//
//        for (int i = 0; i < dates.size(); i++) {
//            CanDelete cd = new CanDelete();
//            cd.setId((long) i + 5);
//            cd.setPostDate(dates.get(i).toString());
//            cd.setEffectiveDate(dates.get(i).toString());
//            cd.setAmount(0.0);
//            cd.setAccountBalance(0.0);
//            list2.add(cd);
//           // canDeletes.addAll(list2);
////            canDeletes.add(cd);
////            setValues.add(cd);
//        }
//        for (CanDelete canDelete : list2) {
//            System.out.println("temp data is: " + canDelete);
//        }
//
//        final Serde<String> keySerde = Serdes.String();
//        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
//        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
//        KTable<String, Long> counts = test.groupByKey().count();
//        String key = "my-key";
//        Long count = counts.get(key);
//        int countAsInt = count.intValue();
//
//        test.filter(new Predicate<String, CanDelete>() {
//            @Override
//            public boolean test(String key, CanDelete value) {
//
//                String startDate;
//                String lastDate;
//
//                if (value.getPostDate()>startDate && value.getPostDate()<lastDate){
//                    int days = value.getPostDate()-startDate;
//                    for (int i=0; i=days; i++){ //for adding the data for 1,2
//                        CanDelete cd = new CanDelete();
//                        cd.setId((long) i + 5);
//                        cd.setPostDate(dates.get(i).toString());
//                        cd.setEffectiveDate(dates.get(i).toString());
//                        cd.setAmount(0.0);
//                        cd.setAccountBalance(0.0);
//                        list2.add(cd);
//                    }
//                    //for adding the data for 3rd
//                    CanDelete canDelete = new CanDelete();
//                    canDelete.setId(value.getId());
//                    canDelete.setPostDate(value.getPostDate());
//                    canDelete.setAmount(value.getAmount());
//                    canDelete.setEffectiveDate(value.getEffectiveDate());
//                    canDelete.setAccountBalance(balance);
//                    boolean add = list2.add(canDelete);
//
//                    startDate = value.getPostDate()+1;
//                    countAsInt= countAsInt-1;
//                }
//                if (value.getPostDate()=startDate){
//                    //for adding the data for 4th
//                    CanDelete canDelete = new CanDelete();
//                    canDelete.setId(value.getId());
//                    canDelete.setPostDate(value.getPostDate());
//                    canDelete.setAmount(value.getAmount());
//                    canDelete.setEffectiveDate(value.getEffectiveDate());
//                    canDelete.setAccountBalance(balance);
//                    boolean add = list2.add(canDelete);
//
//                    startDate = value.getPostDate()+1;
//                    countAsInt= countAsInt-1;
//                }
//                if (value.getPostDate()=lastDate){
//                    //for adding the data for 4th
//                    CanDelete canDelete = new CanDelete();
//                    canDelete.setId(value.getId());
//                    canDelete.setPostDate(value.getPostDate());
//                    canDelete.setAmount(value.getAmount());
//                    canDelete.setEffectiveDate(value.getEffectiveDate());
//                    canDelete.setAccountBalance(balance);
//                    boolean add = list2.add(canDelete);
//
//                    startDate = value.getPostDate()+1;
//                    countAsInt= countAsInt-1;
//                }
//                if (value.getPostDate()<lastDate && countAsInt==0){
//                    int days = lastDate-value.getPostDate();
//                    for (int i=0; i=days; i++){ //for adding the data for 5,6,7
//                        CanDelete cd = new CanDelete();
//                        cd.setId((long) i + 5);
//                        cd.setPostDate(dates.get(i).toString());
//                        cd.setEffectiveDate(dates.get(i).toString());
//                        cd.setAmount(0.0);
//                        cd.setAccountBalance(0.0);
//                        list2.add(cd);
//                    }
//
//                }
//
////        test.filter(new Predicate<String, CanDelete>() {
////            @Override
////            public boolean test(String key, CanDelete value) {
////
////                String startDate;
////                String lastDate;
////
////                if (value.getPostDate()>startDate && value.getPostDate()<lastDate){
////                    int days = value.getPostDate()-startDate;
////                    for (int i=0; i=days; i++){ //for adding the data for 1,2
////                        CanDelete cd = new CanDelete();
////                        cd.setId((long) i + 5);
////                        cd.setPostDate(dates.get(i).toString());
////                        cd.setEffectiveDate(dates.get(i).toString());
////                        cd.setAmount(0.0);
////                        cd.setAccountBalance(0.0);
////                        list2.add(cd);
////                    }
////                    //for adding the data for 3rd
////                    CanDelete canDelete = new CanDelete();
////                    canDelete.setId(value.getId());
////                    canDelete.setPostDate(value.getPostDate());
////                    canDelete.setAmount(value.getAmount());
////                    canDelete.setEffectiveDate(value.getEffectiveDate());
////                    canDelete.setAccountBalance(balance);
////                    boolean add = list2.add(canDelete);
////
////                    startDate = value.getPostDate()+1;
////                    countAsInt= countAsInt-1;
////                }
////                if (value.getPostDate()=startDate){
////                    //for adding the data for 4th
////                    CanDelete canDelete = new CanDelete();
////                    canDelete.setId(value.getId());
////                    canDelete.setPostDate(value.getPostDate());
////                    canDelete.setAmount(value.getAmount());
////                    canDelete.setEffectiveDate(value.getEffectiveDate());
////                    canDelete.setAccountBalance(balance);
////                    boolean add = list2.add(canDelete);
////
////                    startDate = value.getPostDate()+1;
////                    countAsInt= countAsInt-1;
////                }
////                if (value.getPostDate()=lastDate){
////                    //for adding the data for 4th
////                    CanDelete canDelete = new CanDelete();
////                    canDelete.setId(value.getId());
////                    canDelete.setPostDate(value.getPostDate());
////                    canDelete.setAmount(value.getAmount());
////                    canDelete.setEffectiveDate(value.getEffectiveDate());
////                    canDelete.setAccountBalance(balance);
////                    boolean add = list2.add(canDelete);
////
////                    startDate = value.getPostDate()+1;
////                    countAsInt= countAsInt-1;
////                }
////                if (value.getPostDate()<lastDate && countAsInt==0){
////                    int days = lastDate-value.getPostDate();
////                    for (int i=0; i=days; i++){ //for adding the data for 5,6,7
////                        CanDelete cd = new CanDelete();
////                        cd.setId((long) i + 5);
////                        cd.setPostDate(dates.get(i).toString());
////                        cd.setEffectiveDate(dates.get(i).toString());
////                        cd.setAmount(0.0);
////                        cd.setAccountBalance(0.0);
////                        list2.add(cd);
////                    }
////
////                }
//
//
//
//
//
//                balance += value.getAmount();
//                CanDelete canDelete = new CanDelete();
//                canDelete.setId(value.getId());
//                canDelete.setPostDate(value.getPostDate());
//                canDelete.setAmount(value.getAmount());
//                canDelete.setEffectiveDate(value.getEffectiveDate());
//                canDelete.setAccountBalance(balance);
//                boolean add = list2.add(canDelete);
////                canDeletes.add(canDelete);
//                list2.forEach(a-> System.out.println("data is: "+a));
////                kafkaTemplate.send("topic5",canDelete);
//                return add;
//            }
//        });
//
//        System.out.println("SetSize: "+canDeletes.size());
////        kafkaTemplate.send("topic5",canDelete);
//
//        System.out.println("listSize:3 "+list2.size());
//
////        }).groupBy((KeyValueMapper<String, CanDelete, CanDelete>) (key, value) -> {
////            for (CanDelete canDelete : list2) {
////                System.out.println("sending data is: "+canDelete);
////               // kafkaTemplate.send("topic5",canDelete);
////            }
////
////            return null;
////        }).count().toStream().to("topic5");
//
////        test.filter((key,value)->{
////            balance += value.getAmount();
////            CanDelete canDelete= new CanDelete();
////            canDelete.setId(value.getId());
////            canDelete.setPostDate(value.getPostDate());
////            canDelete.setAmount(value.getAmount());
////            canDelete.setEffectiveDate(value.getEffectiveDate());
////            canDelete.setAccountBalance(balance);
////            list2.add(canDelete);
////            list2.forEach(a-> System.out.println("list inside data: "+a));
////            return false;
////        }).to("topic5",Produced.with(keySerde, canDeleteSerde));
//
////        test.foreach((key, value) -> {
////            CanDelete canDelete = new CanDelete();
////            balance += value.getAmount();
////            System.out.println("value is: " + value);
////            System.out.println("size of list inside the loop is: " + list2.size());
////            canDelete.setId(value.getId());
////            canDelete.setPostDate(value.getPostDate());
////            canDelete.setAmount(value.getAmount());
////            canDelete.setEffectiveDate(value.getEffectiveDate());
////            canDelete.setAccountBalance(balance);
////            System.out.println("object is: " + canDelete);
////            list2.add(canDelete);
////            list2.forEach(a-> System.out.println("final data is: "+a));
////        });
//
//        kStreamConfig.topology(builder);
//        return null;
//
//    }


//    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
//        log.info("**** start date: *****: " + startDate);
//        log.info("**** end date *****: " + endDate);
//        StreamsBuilder builder = new StreamsBuilder();
//
//        long days = ChronoUnit.DAYS.between(startDate, endDate);
//        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
//                .limit(days)
//                .mapToObj(i -> startDate.plusDays(i))
//                .collect(Collectors.toList());
//        System.out.println("date list size: " + dates.size());
//
//        for (int i = 0; i < dates.size(); i++) {
//            CanDelete cd = new CanDelete();
//            cd.setId((long) i + 5);
//            cd.setPostDate(dates.get(i).toString());
//            cd.setEffectiveDate(dates.get(i).toString());
//            cd.setAmount(0.0);
//            cd.setAccountBalance(0.0);
//            list2.add(cd);
////            setValues.add(cd);
//        }
//        for (CanDelete canDelete : list2) {
//            System.out.println("temp data is: " + canDelete);
//        }
//
//        final Serde<String> keySerde = Serdes.String();
//        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
//        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
//
////        list2.forEach(a-> System.out.println("Before stream data list:"+a));
////        test.foreach((key,value)-> System.out.println("value is: "+value));
//
//
//        test.foreach((key, value) -> {
//            CanDelete canDelete = new CanDelete();
//            balance += value.getAmount();
//            System.out.println("value is: " + value);
//            System.out.println("size of list inside the loop is: " + list2.size());
//            canDelete.setId(value.getId());
//            canDelete.setPostDate(value.getPostDate());
//            canDelete.setAmount(value.getAmount());
//            canDelete.setEffectiveDate(value.getEffectiveDate());
//            canDelete.setAccountBalance(balance);
//            System.out.println("object is: " + canDelete);
//            list2.add(canDelete);
//            list2.forEach(a-> System.out.println("final data is: "+a));
//
////            setValues = new HashSet<>(list2.stream()
////                    .collect(Collectors.toMap(CanDelete::getPostDate, Function.identity(),
////                            (left, right) -> left))
////                    .values());
////            setValues.forEach(a-> System.out.println("set data is: "+a));
//
//
////            list2.forEach(a-> System.out.println("list data is: "+a));
////            int a=1;
////            for(CanDelete canDelete1: list2){
////                System.out.println("object post date: "+canDelete.getPostDate());
////                System.out.println("list object post date: "+canDelete1.getPostDate());
////                if(canDelete.getPostDate().toString().equalsIgnoreCase(canDelete1.getPostDate().toString())){
////                    System.out.println("loop run for: "+a);
////                        list2.remove(canDelete1);
////                        a++;
//////                    list2.add(canDelete);
////                }
////            }
////            for(CanDelete canDelete1: list2){
////                System.out.println("list size: "+list2.size());
////                System.out.println("latest data : "+canDelete1);
////                setValues.add(canDelete1);
////            }
////            System.out.println("set size: "+setValues.size());
////            setValues.forEach(a-> System.out.println("set values: "+a));
////            for(CanDelete canDelete1: setValues){
////                if(canDelete1.getPostDate().equalsIgnoreCase(canDelete.getPostDate()) && canDelete.getAmount()>0.0){
////                    list2.remove(canDelete1);
////                    System.out.println("size of list after remove: "+ list2.size());
////                    balance+=canDelete.getAmount();
////                    System.out.println("Balance is: "+balance);
////                    canDelete.setAccountBalance(balance);
////                    System.out.println("size of after list: "+ list2.size());
////                    list2.add(canDelete);
////                    System.out.println("size of after adding list: "+ list2.size());
////                }
////            }
////            list2.forEach(a-> System.out.println("list data is: "+a));
//
//
////            set.add(canDelete);
////            list2.add(canDelete);
//
//        });
////        test.foreach((key,value)->list2.add());
////        list2.forEach(a -> System.out.println("After adding data: " + a));
//
//
////        KTable<String, CanDelete> kTable = builder.table("test3",
////                Materialized.<String, CanDelete, KeyValueStore<Bytes, byte[]>>as("ktable-store3")
////                        .withKeySerde(Serdes.String())
////                        .withValueSerde(canDeleteSerde));
//
//
//        kStreamConfig.topology(builder);
//        return null;
//
//    }


//    public List<CanDelete> getDataBetweenDates(LocalDate startDate, LocalDate endDate) {
//        log.info("**** start date: *****: " + startDate);
//        log.info("**** end date *****: " + endDate);
//        StreamsBuilder builder = new StreamsBuilder();
//
//        long days = ChronoUnit.DAYS.between(startDate, endDate);
//        List<LocalDate> dates = IntStream.iterate(0, i -> i + 1)
//                .limit(days)
//                .mapToObj(i -> startDate.plusDays(i))
//                .collect(Collectors.toList());
//
//
//        for (int i = 0; i < dates.size(); i++) {
//            CanDelete cd = new CanDelete();
//            cd.setId((long) i);
//            cd.setPostDate(dates.get(i).toString());
//            cd.setEffectiveDate(dates.get(i).toString());
//            cd.setAmount(0.0);
//            cd.setAccountBalance(0.0);
//            list2.add(cd);
//        }
//
//        final Serde<String> keySerde = Serdes.String();
//        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
//        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
//
//        // Print the List
//        final double total = 0;
//
//
////        test.peek((key,value)->{
////            list2.add(value);
////            list2.forEach(a->System.out.println("inside value of list is: "+a));
////        }).foreach((key,value)->{});
//
//        list2.forEach(a->System.out.println("value of list is: "+a));
//
////            System.out.println("Key: " + key + ", Value: " + value);
////
////
////            Map<Long, CanDelete> temp = list2.stream()
////                    .collect(Collectors.toMap(
////                            CanDelete::getId,
////                            Function.identity(),
////                            (object1, object2) -> object1
////                    ));
////
////            for (CanDelete object : list2) {
////                if (object.getAmount() > 0.0) {
////                    temp.merge(object.getId(), object, (oldObject, newObject) -> newObject);
////                }
////            }
////            DecimalFormat decimalFormat = new DecimalFormat("#.##");
////            balance = setBalance(value);
////            decimalFormat.format(balance);
////            System.out.println("balance amount is: " + balance);
////            outputList.addAll(temp.values());
////            for (CanDelete canDelete1 : outputList) {
////                System.out.println("data details: " + canDelete1);
////                if (canDelete1.getAmount() != 0.0) {
////                    canDelete1.setAccountBalance(Double.parseDouble(decimalFormat.format(balance)));
////                }
////                kafkaTemplate.send("topic5", canDelete1);
////            }
//
//
//
////            value.setAccountBalance(value.getAccountBalance() + value.getAmount());
//
////        });
////        for (CanDelete canDelete: list2){
////            System.out.println("values are: "+canDelete);
////        }
//
////        test.mapValues(value -> {
////            CanDelete obj = new CanDelete();
////            obj.setId(value.getId());
////            obj.setPostDate(value.getPostDate());
////            obj.setEffectiveDate(value.getEffectiveDate());
////            obj.setAmount(value.getAmount());
////            getObject(obj);
////            return value;
////        });
////        test.mapValues(value -> {
////
////            CanDelete obj = new CanDelete();
////            obj.setId(value.getId());
////            obj.setPostDate(value.getPostDate());
////            obj.setEffectiveDate(value.getEffectiveDate());
////            obj.setAmount(value.getAmount());
////            getObject(obj);
////            return value;
////        });
////        }).to("test5", Produced.with(keySerde, canDeleteSerde));
//        kStreamConfig.topology(builder);
//        return null;
//
//    }


//    public List<CanDelete> getObject(CanDelete canDelete) {
//        list2.add(canDelete);
//        Map<Long, CanDelete> temp = list2.stream()
//                .collect(Collectors.toMap(
//                        CanDelete::getId,
//                        Function.identity(),
//                        (object1, object2) -> object1
//                ));
//
//        for (CanDelete object : list2) {
//            if (object.getAmount() > 0.0) {
//                temp.merge(object.getId(), object, (oldObject, newObject) -> newObject);
//            }
//        }
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//        balance = setBalance(canDelete);
//        decimalFormat.format(balance);
//        System.out.println("balance amount is: " + balance);
//        outputList.addAll(temp.values());
//        for (CanDelete canDelete1 : outputList) {
//            System.out.println("data details: " + canDelete1);
//            if (canDelete1.getAmount() != 0.0) {
//                canDelete1.setAccountBalance(Double.parseDouble(decimalFormat.format(balance)));
//            }
//            kafkaTemplate.send("topic5", canDelete1);
//        }
//        return outputList;
//    }


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
