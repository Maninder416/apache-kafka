package com.optum.labs.kafka;

import com.optum.labs.kafka.service.DataGenerationService;
import com.optum.labs.kafka.streams.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SpringBootKafkaApplication implements CommandLineRunner {
    @Value("${kafka.schema-registry}")
    public String SCHEMA_REGISTRY_URL;
    @Value("${kafka.bootstrap-server}")
    public String KAFKA_BOOTSTRAP_SERVER;
    @Value("${kafka.application-id}")
    public String PRODUCT_CATEGORY_APP_ID;
    @Autowired
    private DataGenerationService dataCreationService;
    @Autowired
    private ProductCategoryCodeOutput11 productCategoryCodeOutput11;
    @Autowired
    private CurrencyCodeLoanTxnStream8 currencyCodeLoanTxnStream8;
    @Autowired
    private CreditLineCurrencyProductCategoryStream12 creditLineCurrencyProductCategoryStream12;
    @Autowired
    private CreditCreditLineDetailsStream3 creditCreditLineDetailsStream3;
    @Autowired
    private CreditLineAndActivityOutStream14 creditLineAndActivityOutStream14;
    @Autowired
    private FlexCreditLineActivityOutputStream15 flexCreditLineActivityOutputStream15;
    @Autowired
    private CreditLineFlexActivityStream5 creditLineFlexActivityStream5;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaApplication.class, args);
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
        dataCreationService.generateDataForFlexActivity();
        log.info("******** Data is inserted into tables ********");
        log.info("******* Trying to send streaming data *******");
        creditCreditLineDetailsStream3.creditLineDetails();
        creditLineFlexActivityStream5.flexCreditLineActivityStream();
        creditLineAndActivityOutStream14.flexCreditLineStream();
        currencyCodeLoanTxnStream8.currencyCodeLoanTxnActivityStream();
        productCategoryCodeOutput11.productCategoryCodeStream();
        creditLineCurrencyProductCategoryStream12.creditLinesCurrencyLoanTxn12();
        flexCreditLineActivityOutputStream15.flexCreditLineActivityLoanTxnOutputStream();

    }
}
