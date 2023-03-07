package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.output.CurrencyCodeLoanTxnActivityOutput;
import com.optum.labs.kafka.entity.output.CurrencyLoanProductCategoryCodeOutput;
import com.optum.labs.kafka.entity.output.ProductCategory;
import com.optum.labs.kafka.utils.TopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class CreditLineCurrencyProductCategoryStream12 {
    @Autowired
    private KStreamConfig kStreamConfig;

    public void creditLinesCurrencyLoanTxn12() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<ProductCategory> productCategorySerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ProductCategory.class));

        KStream<String, ProductCategory> productCategoryKStream = builder.stream(TopicEnum.PRODUCT_CATEGORY_DETAILS_11_TOPIC.getTopicName(), Consumed.with(Serdes.String(), productCategorySerde));
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
                builder.stream(TopicEnum.CURRENCY_LOAN_8_TOPIC.getTopicName(), Consumed.with(Serdes.String(),
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

        currencyLoanProductCategoryCodeOutputKStream.to(TopicEnum.CURRENCY_LOAN_PRODUCT_CATEGORY_12_TOPIC.getTopicName(), Produced.with(Serdes.String(),
                new JsonSerde<>(CurrencyLoanProductCategoryCodeOutput.class)));
        kStreamConfig.topology(builder);
    }
}
