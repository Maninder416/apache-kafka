package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.FlexActivity;
import com.optum.labs.kafka.entity.output.CreditLineActivityOutput14;
import com.optum.labs.kafka.entity.output.CreditLineFlexFeeOutput5;
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
public class CreditLineAndActivityOutStream14 {

    @Autowired
    private KStreamConfig kStreamConfig;

    public void flexCreditLineStream() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<FlexActivity> flexActivitySerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(FlexActivity.class));
        KStream<String, FlexActivity> flexActivityKStream = builder.stream(TopicEnum.FLEX_CREDITLINE_TOPIC_13_INPUT.getTopicName(), Consumed.with(Serdes.String(), flexActivitySerde));
        flexActivityKStream.print(Printed.toSysOut());
        flexActivityKStream.foreach((key, value) ->
                log.info("***** key and value for flex activity stream is ***** : :{} :{}", key, value));

        KStream<String, FlexActivity> flexActivityStreamInfo = flexActivityKStream.selectKey((key, value) ->
                value.getId().toString());

        log.info("**** value of id ****",flexActivityStreamInfo.toString());

        final Serde<CreditLineFlexFeeOutput5> flexFeeOutput5Serde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(CreditLineFlexFeeOutput5.class));
        KStream<String, CreditLineFlexFeeOutput5> flexFeeOutput5KStream = builder.stream(TopicEnum.FLEX_CREDIT_LINE_ACTIVITY_OUT_5_TOPIC.getTopicName(), Consumed.with(Serdes.String(), flexFeeOutput5Serde));
        flexFeeOutput5KStream.print(Printed.toSysOut());
        flexFeeOutput5KStream.foreach((key, value) ->
                log.info("**** key and value for flex fee output stream *****: :{} :{}", key, value));

        KStream<String, CreditLineFlexFeeOutput5> flexFeeOutput5KStreamInfo =
                flexFeeOutput5KStream.selectKey((key, value) ->
                        value.getId().toString());

        ValueJoiner<FlexActivity, CreditLineFlexFeeOutput5, CreditLineActivityOutput14> joiner =
                (flexActivity, creditLineDetails) ->
                        CreditLineActivityOutput14.builder()
                                .id(flexActivity.getId())
                                .customerLineNumber(flexActivity.getCustomerLineNumber())
                                .postDt(creditLineDetails.getPostDt())
                                .cif(creditLineDetails.getCif())
                                .effdt(creditLineDetails.getEffdt())
                                .flex_cmtmnt_amt_lcy(creditLineDetails.getFlex_cmtmnt_amt_lcy())
                                .flex_cmtmnt_amt_tcy(creditLineDetails.getFlex_cmtmnt_amt_tcy())
                                .flex_unCmtMnt_amt_lcy(creditLineDetails.getFlex_unCmtMnt_amt_lcy())
                                .flex_unCmtMnt_amt_tcy(creditLineDetails.getFlex_unCmtMnt_amt_tcy())
                                .flex_fee_pct(creditLineDetails.getFlex_fee_pct())
                                .flex_fee_accr_bas(creditLineDetails.getFlex_fee_accr_bas())
                                .trans_crrncy_cd(creditLineDetails.getTrans_crrncy_cd())
                                .entity(creditLineDetails.getEntity())
                                .applId(creditLineDetails.getApplId())
                                .src_updt_dt(creditLineDetails.getSrc_updt_dt())
                                .dw_create_ts(creditLineDetails.getDw_create_ts())
                                .created_by(creditLineDetails.getCreated_by())
                                .applId_loan(creditLineDetails.getApplId_loan())
                                .creditLineStatus(creditLineDetails.getCreditLineStatus())
                                .psgl_department(creditLineDetails.getPsgl_department())
                                .branchNbr(creditLineDetails.getBranchNbr())
                                .cba_aoteamcd(creditLineDetails.getCba_aoteamcd())
                                .nameAddRln1(creditLineDetails.getNameAddRln1())
                                .nameAddRln2(creditLineDetails.getNameAddRln2())
                                .nameAddRln3(creditLineDetails.getNameAddRln3())
                                .nameAddRln4(creditLineDetails.getNameAddRln4())
                                .nameAddRln5(creditLineDetails.getNameAddRln5())
                                .nameAddRln6(creditLineDetails.getNameAddRln6())
                                .zipPostalCd(creditLineDetails.getZipPostalCd())
                                .fullName(creditLineDetails.getFullName())
                                .statusCd(creditLineDetails.getStatusCd())
                                .expiryDate(creditLineDetails.getExpiryDate())
                                .build();

        KStream<String, CreditLineActivityOutput14> creditLineActivityOutput14KStream =
                flexActivityStreamInfo.join(flexFeeOutput5KStreamInfo, joiner,
                        JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), flexActivitySerde, flexFeeOutput5Serde));

        creditLineActivityOutput14KStream.print(Printed.toSysOut());
        creditLineActivityOutput14KStream.foreach((key, value) ->
                log.info("***** key and value output for creditLineActivityOutput14KStream *****: :{} :{}", key, value)
        );
        creditLineActivityOutput14KStream.to(TopicEnum.FLEX_CREDIT_LINE_AND_ACTIVITY_OUT_14_TOPIC.getTopicName(), Produced.with(Serdes.String(), new JsonSerde<>(CreditLineActivityOutput14.class)));
        kStreamConfig.topology(builder);
    }
}
