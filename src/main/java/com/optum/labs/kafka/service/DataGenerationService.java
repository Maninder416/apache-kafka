package com.optum.labs.kafka.service;

import com.github.javafaker.Faker;
import com.optum.labs.kafka.config.KafkaProducerConfig;
import com.optum.labs.kafka.entity.*;
import com.optum.labs.kafka.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

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
    @Autowired
    private CanDeleteRepository canDeleteRepository;

//    LocalDate startDate = LocalDate.of(2022, 3, 1);
//    LocalDate endDate = LocalDate.of(2022, 6, 1);

    LocalDate startDate = LocalDate.of(2022, 1, 1);
    LocalDate endDate = LocalDate.of(2022, 1, 5);


    /**
     * Inserting data for Loan entity
     *
     * @return
     */
    public String generateDataForLoan() {
        for (int i = 0; i <= 10; i++) {
            Loan loan = new Loan();
            loan.setAccountNumber(Integer.valueOf(faker.number().digits(2)));
            String pattern = "[L-O]{2}";
            loan.setApplId(faker.regexify(pattern));
            loan.setAccountNumberCreditLine(Integer.valueOf(faker.number().digits(2)));
            loan.setFaceAmtoFnoteOrgnlbal_tcy(faker.number().randomDouble(0, 1000, 10000));
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
        for (int i = 0; i <= 10; i++) {
            CreditLines creditLines = new CreditLines();
            String pattern = "[L-O]{2}";
            String pattern2 = "[L-O]{1}";
            String currencyCode = "[A-D]{3}";
            creditLines.setApplId_loan(faker.regexify(pattern));
            creditLines.setCreditLineStatus(faker.regexify(pattern2));
            creditLines.setCustLineNbr((faker.number().digits(2)));
            creditLines.setApplId(faker.regexify(currencyCode));
//            creditLines.setPostDt(faker.date().birthday());
            creditLines.setPostDt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
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
        for (int i = 0; i < 10; i++) {
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
        for (int i = 0; i < 10; i++) {
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
        for (int i = 0; i <= 10; i++) {
            PsRtRate rate = new PsRtRate();
            rate.setTo_cur(faker.number().digits(2));
            rate.setFrom_cur(faker.number().digits(2));
            rate.setSvb_rate(faker.number().randomDouble(0, 1000, 10000));
//            rate.setEffdt(faker.date().birthday());
            rate.setEffdt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
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
        for (int i = 0; i <= 10; i++) {
            TestHolidayCalendar calendar = new TestHolidayCalendar();
//            calendar.setBranchHolidayDt(faker.date().birthday());
            calendar.setBranchHolidayDt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
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
        for (int i = 0; i <= 10; i++) {
            TestLoanTransHist hist = new TestLoanTransHist();
            hist.setAcctNbr(faker.number().digits(2));
//            hist.setEffectiveDt(faker.date().birthday());
//            hist.setPostDt(faker.date().birthday());
            hist.setEffectiveDt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            hist.setPostDt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
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
        for (int i = 0; i <= 10; i++) {
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
//            client.setExpiryDate(faker.date().birthday());
            client.setExpiryDate(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            client.setNameAddRln1(faker.regexify(nameAddLn1));
            client.setNameAddRln2(faker.regexify(nameAddLn2));
            client.setNameAddRln3(faker.regexify(nameAddLn3));
            client.setNameAddRln4(faker.regexify(nameAddLn4));
//            client.setExpiryDate(faker.date().birthday());
            client.setExpiryDate(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
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
        for (int i = 0; i <= 10; i++) {
            FlexFeeActivity flexFeeActivity = new FlexFeeActivity();
            String pattern = "[L-O]{3}";
            String currencyCode = "[A-D]{3}";
            flexFeeActivity.setApplId(faker.regexify(pattern));
            flexFeeActivity.setCif(faker.number().digits(2));
            flexFeeActivity.setCreated_by(faker.number().digits(2));
            flexFeeActivity.setCustLnNbr(faker.number().digits(2));
//            flexFeeActivity.setDw_create_ts(faker.date().birthday());
//            flexFeeActivity.setEffdt(faker.date().birthday());
            flexFeeActivity.setDw_create_ts(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            flexFeeActivity.setEffdt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            flexFeeActivity.setEntity(faker.regexify(pattern));
            flexFeeActivity.setFlex_cmtmnt_amt_lcy(faker.number().randomDouble(0, 100, 10000));
            flexFeeActivity.setFlex_cmtmnt_amt_tcy(faker.number().digits(2));
            flexFeeActivity.setFlex_fee_accr_bas(faker.number().digits(2));
            flexFeeActivity.setFlex_fee_pct(faker.number().randomDouble(0, 1000, 10000));
            flexFeeActivity.setFlex_unCmtMnt_amt_lcy(faker.number().digits(2));
            flexFeeActivity.setFlex_unCmtMnt_amt_tcy(faker.number().digits(2));
//            flexFeeActivity.setPostDt(faker.date().birthday());
//            flexFeeActivity.setSrc_updt_dt(faker.date().birthday());
            flexFeeActivity.setPostDt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            flexFeeActivity.setSrc_updt_dt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            flexFeeActivity.setTrans_crrncy_cd(faker.regexify(currencyCode));
            flexFeeRepository.save(flexFeeActivity);
        }
        System.out.println("Kafka template bean: " + kafkaTemplate.toString());
        return "Data generated for FlexFeeActivity table";
    }

    public String generateDataForFlexActivity() {
        for (int i = 0; i < 10; i++) {
            FlexActivity activity = new FlexActivity();
            activity.setCustomerLineNumber((faker.number().digits(2)));
//            activity.setPostDt(faker.date().birthday());
            activity.setPostDt(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            flexActivityRepository.save(activity);
        }
        return "Data generated for flex activity table";
    }

    public String generateCanDeleteData() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 1; i <= 2; i++) {
            //   System.out.println("inside the method");
            CanDelete canDelete = new CanDelete();
            canDelete.setPostDate(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            canDelete.setEffectiveDate(dateFormat(String.valueOf(faker.date().between(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))));
            canDelete.setAmount(faker.number().randomDouble(2,1000,100000));
            //     System.out.println("date format is: "+canDelete.getBirthDate());
            //   dateFormat(canDelete.getDate());
            //  dateFormat2(canDelete.getBirthDate().toString());
            canDeleteRepository.save(canDelete);
//            kafkaTemplate.send("test3",canDelete);
            System.out.println("data send here");
        }
        return "can delete data generated";
    }

    /**
     * Formatting the data into "yyyy-MM-dd" format
     *
     * @param input
     * @return
     */
    public String dateFormat(String input) {
        String outputText = "";
        try {
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            String inputText = "2012-11-17T00:00:00.000-05:00";
            Date date = inputFormat.parse(input);
            outputText = outputFormat.format(date);
        } catch (Exception e) {

        }
        return outputText;
    }


}


