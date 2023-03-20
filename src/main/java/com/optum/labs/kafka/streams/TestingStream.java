package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.CanDelete;
import com.optum.labs.kafka.service.DataGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
    @Autowired
    private DataGenerationService dataGenerationService;
    Double balance = 0.0;
    List<CanDelete> list2 = new ArrayList<>();
    List<CanDelete> oldList = new ArrayList<>();
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
        list2.forEach(a -> System.out.println("fake data present in list: " + a));

        final Serde<String> keySerde = Serdes.String();
        final Serde<CanDelete> canDeleteSerde = new JsonSerde<>(CanDelete.class);
        KStream<String, CanDelete> test = builder.stream("test3", Consumed.with(keySerde, canDeleteSerde));
        test.filter(new Predicate<String, CanDelete>() {
            @Override
            public boolean test(String key, CanDelete value) {
                System.out.println("value incoming post date: " + value.getPostDate());
                System.out.println("value amount: " + value.getAmount());
                balance += value.getAmount();
                System.out.println("balance here is: " + balance);
                value.setAccountBalance(dataFormat(balance));
                System.out.println("account balance: " + value.getAccountBalance());
                System.out.println("here list size is: " + list2.size());
                for (int i = 0; i < list2.size(); i++) {
                    System.out.println("list post date is: " + list2.get(i).getPostDate());
                    System.out.println("value post date is: " + value.getPostDate());
                    System.out.println("value amount here is: " + value.getAmount());
                    if (list2.get(i).getPostDate().equalsIgnoreCase(value.getPostDate()) && value.getAmount() > 0.0) {
                        System.out.println("inside else if block");
                        System.out.println("previous account balance is: " + balance);
                        list2.remove(i);
//                        balance += value.getAmount();
                        //value.setAccountBalance(dataFormat(balance));
                        System.out.println("inside loop value get amount: " + value.getAmount());
                        list2.add(value);
                        System.out.println("================================================");
                    } else if (!(list2.get(i).getPostDate().equalsIgnoreCase(value.getPostDate()))) {
                        System.out.println("valuess are: " + list2.get(i).toString());
                        System.out.println("inside this loop");
                        list2.get(i).setAccountBalance(dataFormat(balance));
                        list2.forEach(a -> System.out.println("temp data: " + a));
                    }

                }
                list2.forEach(a -> System.out.println("here new list is: " + a));
                return true;
            }
        });

        kStreamConfig.topology(builder);
        return null;
    }


    //    @Scheduled(fixedDelay = 60000)
    @Scheduled(initialDelay = 1000 * 30, fixedDelay = Long.MAX_VALUE)
    public void setUpdatedData() {
        for (CanDelete canDelete : list2) {
            oldList.add(canDelete);
            System.out.println("****** sending messages *******");
            kafkaTemplate.send("topic10", canDelete);
        }
    }

    /**
     * To display the list for testing purpose
     *
     * @return
     */
    public List<CanDelete> getAllListElement() {
        return list2;
    }

    /**
     * set the format for balance amount. Only 2 decimals
     *
     * @param value
     * @return
     */
    public double dataFormat(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double tmp = Double.parseDouble(decimalFormat.format(value));
        System.out.println("tmp balance: " + tmp);
        return Double.parseDouble(decimalFormat.format(value));
    }

    public Integer consumerCode() {
        KafkaConsumer<String, CanDelete> consumer = new KafkaConsumer<String, CanDelete>(kStreamConfig.properties());
        consumer.subscribe(Collections.singleton("test3"));

        try {
            while (true) {
                ConsumerRecords<String, CanDelete> records = consumer.poll(Duration.ofMillis(100));
                if (records.count() == 0) break;
                count2 += records.count();
                System.out.println("Count value is: " + count2);
            }
        } catch (Exception e) {

        } finally {
            consumer.close();
        }
        return count2;
    }
}
