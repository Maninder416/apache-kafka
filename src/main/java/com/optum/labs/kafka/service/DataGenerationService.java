package com.optum.labs.kafka.service;

import com.github.javafaker.Faker;
import com.optum.labs.kafka.config.KafkaProducerConfig;
import com.optum.labs.kafka.entity.*;
import com.optum.labs.kafka.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataGenerationService {
    @Autowired
    private Faker faker;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private CreditLineRepository creditLineRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private BpaProductCodeRepository bpaProductCodeRepository;
    @Autowired
    private PsRateRepository psRateRepository;
    @Autowired
    private TestHolidayCalendarRepository testHolidayCalendarRepository;
    @Autowired
    private TestLoanTransHistRepository testLoanTransHistRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FlexFeeRepository flexFeeRepository;
    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private FlexActivityRepository flexActivityRepository;

    /**
     * Inserting data for Loan entity
     *
     * @return
     */
    public String generateDataForLoan() {
        for (int i = 0; i <= 500; i++) {
            Loan loan = new Loan();
            loan.setAccountNumber(Integer.valueOf(faker.number().digits(2)));
            String pattern = "[L-O]{2}";
            loan.setApplId(faker.regexify(pattern));
            loan.setAccountNumberCreditLine(Integer.valueOf(faker.number().digits(2)));
            loan.setFaceAmtoFnoteOrgnlbal_tcy(faker.number().randomDouble(0, 1000, 10000));
          //  kafkaTemplate.send("testing-1",loan.toString());
            loanRepository.save(loan);
        }
        return "Data generated for loan fact table";
    }

    /**
     * Inserting data for credit line entity
     *
     * @return
     */
    public String generateDataForCreditLine() {
        for (int i = 0; i <= 500; i++) {
            CreditLines creditLines = new CreditLines();
            String pattern = "[L-O]{2}";
            String pattern2 = "[L-O]{1}";
            String currencyCode = "[A-D]{3}";
            creditLines.setApplId_loan(faker.regexify(pattern));
            creditLines.setCreditLineStatus(faker.regexify(pattern2));
            creditLines.setCustLineNbr((faker.number().digits(2)));
            creditLines.setApplId(faker.regexify(currencyCode));
            creditLines.setPostDt(faker.date().birthday());
            creditLines.setCif(faker.number().digits(2));
            creditLines.setTrans_crrncy_cd(faker.regexify(currencyCode));
            creditLines.setFlex_fee_debit_acc(faker.number().digits(2));
            creditLineRepository.save(creditLines);
        }
        return "Data generated for credit line table";
    }

    /**
     * Inserting data for instrument entity
     *
     * @return
     */
    public String generateDataForInstrument() {
        for (int i = 0; i < 5; i++) {
            Instrument instrument = new Instrument();
            String pattern = "[L-O]{2}";
            String currencyCode = "[A-D]{3}";
            instrument.setAccountNumber(faker.number().digits(2));
            instrument.setApplId(faker.regexify(pattern));
            instrument.setCif(faker.number().digits(2));
            instrument.setCurrencyCode(faker.regexify(currencyCode));
            instrument.setProdCd(faker.regexify(currencyCode));
            instrumentRepository.save(instrument);
        }
        return "Data generated for instrument table";
    }

    /**
     * Inserting data for BpaUlfProductCodes entity
     *
     * @return
     */
    public String generateDataForbpaUlfProductCodes() {
        for (int i = 0; i < 500; i++) {
            BpaUlfProductCodes bpaUlfProductCodes = new BpaUlfProductCodes();
            String pattern = "[A-D]{3}";
            String category_cd = "[A-D]{2}";
            bpaUlfProductCodes.setProduct_cd(faker.regexify(pattern));
            bpaUlfProductCodes.setProduct_category_cd(faker.regexify(category_cd));
            bpaProductCodeRepository.save(bpaUlfProductCodes);
        }
        return "Data generated for BpaUlfProductCodes table";
    }

    /**
     * Inserting data for PsRate entity
     *
     * @return
     */
    public String generateDataForpsRate() {
        for (int i = 0; i <= 500; i++) {
            PsRtRate rate = new PsRtRate();
            rate.setTo_cur(faker.number().digits(2));
            rate.setFrom_cur(faker.number().digits(2));
            rate.setSvb_rate(faker.number().randomDouble(0, 1000, 10000));
            rate.setEffdt(faker.date().birthday());
            psRateRepository.save(rate);
        }
        return "Data generated for PsRate table";
    }

    /**
     * Inserting data for testHolidayCalendar entity
     *
     * @return
     */
    public String generateDataFortestHolidayCalendar() {
        for (int i = 0; i <= 500; i++) {
            TestHolidayCalendar calendar = new TestHolidayCalendar();
            calendar.setBranchHolidayDt(faker.date().birthday());
            testHolidayCalendarRepository.save(calendar);
        }
        return "Data generated for testHolidayCalendar table";
    }

    /**
     * Inserting data for TestLoanTransHist entity
     *
     * @return
     */
    public String generateDataForTestLoanTransHist() {
        for (int i = 0; i <= 5; i++) {
            TestLoanTransHist hist = new TestLoanTransHist();
            hist.setAcctNbr(faker.number().digits(2));
            hist.setEffectiveDt(faker.date().birthday());
            hist.setPostDt(faker.date().birthday());
            hist.setNotePrncplBalgross(faker.number().randomDouble(0, 1000, 10000));
            hist.setTranId(faker.number().digits(2));
            testLoanTransHistRepository.save(hist);
        }
        return "Data generated for TestLoanTransHist table";
    }

    /**
     * Inserting data for Client entity
     *
     * @return
     */
    public String generateDataForClient() {
        for (int i = 0; i <= 5; i++) {
            Client client = new Client();
            String regex = "\\d{2}[A-H]";
            String nameAddLn1 = "CUSTOMER \\d{10}";
            String nameAddLn2 = "ADDRESS \\d{10}";
            String nameAddLn3 = "ADDRESS 2 \\d{10}";
            String nameAddLn4 = "ADDRESS 3 \\d{10}";
            String statusCode = "[A-C]";
            client.setBranchNbr(faker.number().digits(5));
            client.setCba_aoteamcd(faker.regexify(regex));
            client.setCif(faker.number().digits(2));
            client.setFullName(faker.name().fullName());
            client.setExpiryDate(faker.date().birthday());
            client.setNameAddRln1(faker.regexify(nameAddLn1));
            client.setNameAddRln2(faker.regexify(nameAddLn2));
            client.setNameAddRln3(faker.regexify(nameAddLn3));
            client.setNameAddRln4(faker.regexify(nameAddLn4));
            client.setExpiryDate(faker.date().birthday());
            client.setNameAddRln5(faker.address().streetName());
            client.setNameAddRln6(faker.address().cityName());
            client.setStatusCd(faker.regexify(statusCode));
            client.setZipPostalCd(faker.number().digits(7));
            client.setPsgl_department(faker.number().digits(10));
            clientRepository.save(client);
        }
        return "Data generated for Client table";
    }

    /**
     * Inserting data for FlexFeeActivity entity
     *
     * @return
     */
    public String generateDataForFlexFeeActivity() {
        for (int i = 0; i <= 500; i++) {
            FlexFeeActivity flexFeeActivity = new FlexFeeActivity();
            String pattern = "[L-O]{3}";
            String currencyCode = "[A-D]{3}";
            flexFeeActivity.setApplId(faker.regexify(pattern));
            flexFeeActivity.setCif(faker.number().digits(2));
            flexFeeActivity.setCreated_by(faker.number().digits(2));
            flexFeeActivity.setCustLnNbr(faker.number().digits(2));
            flexFeeActivity.setDw_create_ts(faker.date().birthday());
            flexFeeActivity.setEffdt(faker.date().birthday());
            flexFeeActivity.setEntity(faker.regexify(pattern));
            flexFeeActivity.setFlex_cmtmnt_amt_lcy(faker.number().randomDouble(0, 100, 10000));
            flexFeeActivity.setFlex_cmtmnt_amt_tcy(faker.number().digits(2));
            flexFeeActivity.setFlex_fee_accr_bas(faker.number().digits(2));
            flexFeeActivity.setFlex_fee_pct(faker.number().randomDouble(0, 1000, 10000));
            flexFeeActivity.setFlex_unCmtMnt_amt_lcy(faker.number().digits(2));
            flexFeeActivity.setFlex_unCmtMnt_amt_tcy(faker.number().digits(2));
            flexFeeActivity.setPostDt(faker.date().birthday());
            flexFeeActivity.setSrc_updt_dt(faker.date().birthday());
            flexFeeActivity.setTrans_crrncy_cd(faker.regexify(currencyCode));
            flexFeeRepository.save(flexFeeActivity);
        }
        System.out.println("Kafka template bean: " + kafkaTemplate.toString());
        return "Data generated for FlexFeeActivity table";
    }

    public String generateDataForFlexActivity(){
        for(int i=0;i<500;i++){
            FlexActivity activity= new FlexActivity();
            activity.setCustomerLineNumber((faker.number().digits(2)));
            activity.setPostDt(faker.date().birthday());
            flexActivityRepository.save(activity);
        }
        return "Data generated for flex activity table";
    }

}
