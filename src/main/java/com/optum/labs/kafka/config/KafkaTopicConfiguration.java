package com.optum.labs.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic creditLineDetailsTopic() {
        return TopicBuilder.name("credit.creditlines.creditline-detials.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ulfClientDetailsTopic() {
        return TopicBuilder.name("credit.creditlines.ulf-client-detials.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic flexCreditLineActivityTopic() {
        return TopicBuilder.name("credit.creditlines.flex-creditline-activity.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic currencyCodeTopic() {
        return TopicBuilder.name("credit.creditlines.currency-code.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic loanTxtTopic() {
        return TopicBuilder.name("credit.creditlines.loantxn.activity.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productCodeTopic() {
        return TopicBuilder.name("credit.creditlines-product-code.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productCategoryTopic() {
        return TopicBuilder.name("credit.creditlines.product-category.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic flexCreditLineTopic() {
        return TopicBuilder.name("credit.creditlines.flex-creditline.in")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic creditLineDetailsOutTopic() {
        return TopicBuilder.name("credit.creditlines.creditline-detials.out")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic flexCreditLineActivityOutputTopic() {
        return TopicBuilder.name("credit.creditlines.flex-creditline-activity.out")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic currencyCodeLoanTxnActivityTopic() {
        return TopicBuilder.name("credit.creditlines.currency-code-loantxn.activity.out")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productCategoryProductCodeTopic() {
        return TopicBuilder.name("credit.creditlines.product-category-product-code.out")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productCategoryCodeTopic() {
        return TopicBuilder.name("credit.creditlines.currency-loantxn-product-category-code.out")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic flexCreditLineAndActivityTopic() {
        return TopicBuilder.name("credit.creditlines.flex-creditline-and-activity.out")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic creditLineAndActivityAndLoanTxnTopic() {
        return TopicBuilder.name("credit.creditlines.flex-creditline-and-activity-and-loantxn-and-prodcat.out")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic testingTopic() {
        return TopicBuilder.name("testing")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
