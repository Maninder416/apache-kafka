package com.optum.labs.kafka.streams;


import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.CanDelete;
import com.optum.labs.kafka.entity.output.CreditLineLoanTxnProd15;
import com.optum.labs.kafka.utils.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class FinalOutputStream {

    @Autowired
    private KStreamConfig kStreamConfig;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    List<CreditLineLoanTxnProd15> list2 = new ArrayList<>();

    Double balance = 0.0;

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
            CreditLineLoanTxnProd15 cd = new CreditLineLoanTxnProd15();
            cd.setId((long) i);
            cd.setPostDt(dates.get(i).toString());
            cd.setEffdt(dates.get(i).toString());
            cd.setNotePrncplBalgross(0.0);
            cd.setAccountBalance(0.0);
            list2.add(cd);
        }
        list2.forEach(a -> System.out.println("fake data present in list: " + a));

        final Serde<String> keySerde = Serdes.String();
        final Serde<CreditLineLoanTxnProd15> canDeleteSerde = new JsonSerde<>(CreditLineLoanTxnProd15.class);
        KStream<String, CreditLineLoanTxnProd15> test = builder.stream(TopicEnum.CREDIT_LINE_TOPIC_15.getTopicName(), Consumed.with(keySerde, canDeleteSerde));
        test.filter(new Predicate<String, CreditLineLoanTxnProd15>() {
            @Override
            public boolean test(String key, CreditLineLoanTxnProd15 value) {
                for (int i = 0; i < list2.size(); i++) {
                    if (value.getPostDt().toString().equalsIgnoreCase(list2.get(i).getPostDt().toString())) {
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

    @Scheduled(initialDelay = 1000 * 30, fixedDelay = Long.MAX_VALUE)
    public void setUpdatedData() {
        log.info("******* scheduler called *******");
        list2.sort(new Comparator<CreditLineLoanTxnProd15>() {
            @Override
            public int compare(CreditLineLoanTxnProd15 o1, CreditLineLoanTxnProd15 o2) {
                return o1.getPostDt().compareTo(o2.getPostDt());
            }
        });
        for (CreditLineLoanTxnProd15 canDelete : list2) {
            balance += canDelete.getNotePrncplBalgross();
            if (canDelete.getNotePrncplBalgross() > 0.0) {
                canDelete.setAccountBalance(dataFormat(balance));
            }
            if (canDelete.getNotePrncplBalgross() == 0.0) {
                canDelete.setAccountBalance(dataFormat(balance));
            }
            log.info("****** sending messages *******");
            log.info("***** list is: *****" + canDelete);
            kafkaTemplate.send("final-topic", canDelete);
        }
    }

    /**
     * set the format for balance amount. Only 2 decimals
     *
     * @param value
     * @return
     */
    public double dataFormat(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(value));
    }
}
