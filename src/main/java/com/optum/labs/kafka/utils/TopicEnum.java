package com.optum.labs.kafka.utils;

public enum TopicEnum {
    CREDIT_LINE_DETAILS_1_TOPIC("credit.creditlines.creditline-detials.in"),
    CREDIT_LINE_DETAILS_TOPIC_OUTPUT_3_TOPIC("credit.creditlines.creditline-details.out"),
    FLEX_CREDIT_LINE_ACTIVITY_OUT_5_TOPIC("credit.creditlines.flex-creditline-activity.out"),
    FLEX_CREDIT_LINE_ACTIVITY_INPUT("credit.creditlines.flex-creditline-activity.in"),
    PRODUCT_DETAILS_TOPIC("credit.creditlines-product-code.in"),
    CATEGORY_DETAILS_TOPIC("credit.creditlines.product-category.in"),
    PRODUCT_CATEGORY_DETAILS_11_TOPIC("credit.creditlines.product-category-product-code.out"),
    CURRENCY_CODE_6_TOPIC("credit.creditlines.currency-code.in"),
    LOAN_TXN_7_TOPIC("credit.creditlines.loantxn.activity.in"),
    CURRENCY_LOAN_TOPIC("credit.creditlines.currency-code-loantxn.activity.out"),
    CURRENCY_LOAN_PRODUCT_CATEGORY_12_TOPIC("credit.creditlines.currency-loantxn-product-category-code.out"),
    FLEX_CREDIT_LINE_AND_ACTIVITY_OUT_14_TOPIC("credit.creditlines.flex-creditline-and-activity.out"),
    CLIENT_DETAILS_2_TOPIC("credit.creditlines.ulf-client-detials.in"),
    CREDIT_LINE_TOPIC_15("credit.creditlines.flex-creditline-and-activity-and-loantxn-and-prodcat.out"),
    FLEX_CREDITLINE_TOPIC_13_INPUT("credit.creditlines.flex-creditline.in");


    private final String topicName;

    TopicEnum(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
