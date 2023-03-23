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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    Double balance = 0.0;
    List<CanDelete> list2 = new ArrayList<>();
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
            cd.setId((long) i);
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
                for (int i = 0; i < list2.size(); i++) {
                    if (value.getPostDate().toString().equalsIgnoreCase(list2.get(i).getPostDate().toString())) {
                        list2.remove(list2.get(i));
                        list2.add(value);
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
        list2.sort(new Comparator<CanDelete>() {
            @Override
            public int compare(CanDelete o1, CanDelete o2) {
                return o1.getPostDate().compareTo(o2.getPostDate());
            }
        });
        for (CanDelete canDelete : list2) {
            balance += canDelete.getAmount();
            if (canDelete.getAmount() > 0.0) {
                canDelete.setAccountBalance(dataFormat(balance));
            }
            if (canDelete.getAmount() == 0.0) {
                canDelete.setAccountBalance(dataFormat(balance));
            }
            log.info("****** sending messages *******");
            log.info("***** list is: *****" + canDelete);
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
            }
        } catch (Exception e) {

        } finally {
            consumer.close();
        }
        return count2;
    }
}
