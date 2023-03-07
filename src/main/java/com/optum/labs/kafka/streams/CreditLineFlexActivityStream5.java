package com.optum.labs.kafka.streams;

import com.optum.labs.kafka.config.KStreamConfig;
import com.optum.labs.kafka.entity.FlexFeeActivity;
import com.optum.labs.kafka.entity.output.CreditLineFlexFeeOutput5;
import com.optum.labs.kafka.entity.output.CreditLineUserDetailsOutput;
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
public class CreditLineFlexActivityStream5 {
    @Autowired
    private KStreamConfig kStreamConfig;

    /**
     * Creating the stream using topic4 and topic3
     */
    public void flexCreditLineActivityStream() {
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<FlexFeeActivity> flexCreditLineActivityOutputSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(FlexFeeActivity.class));
        KStream<String, FlexFeeActivity> flexCreditLineActivityOutputKStream = builder.stream(TopicEnum.FLEX_CREDIT_LINE_ACTIVITY_INPUT.getTopicName()
                , Consumed.with(Serdes.String(), flexCreditLineActivityOutputSerde));
        flexCreditLineActivityOutputKStream.print(Printed.toSysOut());
        flexCreditLineActivityOutputKStream.foreach((key, value) ->
                log.info("***** key and value for flex activity :{} :{}", key, value));
        KStream<String, FlexFeeActivity> flexCreditLineActivityOutputInfo = flexCreditLineActivityOutputKStream
                .selectKey((key, value) ->
                        value.getId().toString());

        //creating the serdes to receive data from topic3
        final Serde<CreditLineUserDetailsOutput> creditLineUserDetailsOutputSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(CreditLineUserDetailsOutput.class));

        KStream<String, CreditLineUserDetailsOutput> creditLineUserDetailsOutputKStream =
                builder.stream(TopicEnum.CREDIT_LINE_DETAILS_TOPIC_OUTPUT_3_TOPIC.getTopicName(), Consumed.with(Serdes.String(), creditLineUserDetailsOutputSerde));
        creditLineUserDetailsOutputKStream.print(Printed.toSysOut());
        creditLineUserDetailsOutputKStream.foreach((key, value) ->
                log.info("***** credit line user details output: ****** :{} :{}", key, value)
        );
        KStream<String, CreditLineUserDetailsOutput> creditLineUserDetailsOutputInfo =
                creditLineUserDetailsOutputKStream.selectKey((key, value) ->
                        value.getId().toString()
                );

        ValueJoiner<FlexFeeActivity, CreditLineUserDetailsOutput, CreditLineFlexFeeOutput5> joiner =
                (flexFeeActivity, creditLineDetails) -> CreditLineFlexFeeOutput5.builder()
                        .id(flexFeeActivity.getId())
                        .effdt(flexFeeActivity.getEffdt())
                        .postDt(flexFeeActivity.getPostDt())
                        .created_by(flexFeeActivity.getCreated_by())
                        .custLnNbr(flexFeeActivity.getCustLnNbr())
                        .dw_create_ts(flexFeeActivity.getDw_create_ts())
                        .flex_fee_pct(flexFeeActivity.getFlex_fee_pct())
                        .flex_fee_accr_bas(flexFeeActivity.getFlex_fee_accr_bas())
                        .flex_unCmtMnt_amt_lcy(flexFeeActivity.getFlex_unCmtMnt_amt_lcy())
                        .flex_unCmtMnt_amt_tcy(flexFeeActivity.getFlex_unCmtMnt_amt_tcy())
                        .flex_cmtmnt_amt_tcy(flexFeeActivity.getFlex_cmtmnt_amt_tcy())
                        .trans_crrncy_cd(flexFeeActivity.getTrans_crrncy_cd())
                        .entity(flexFeeActivity.getEntity())
                        .src_updt_dt(flexFeeActivity.getSrc_updt_dt())
                        .custLineNbr(creditLineDetails.getCustLineNbr())
                        .applId_loan(creditLineDetails.getApplId_loan())
                        .creditLineStatus(creditLineDetails.getCreditLineStatus())
                        .applId(creditLineDetails.getApplId())
                        .postDt(creditLineDetails.getPostDt())
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
                        .cif(creditLineDetails.getCif())

                        .build();

        KStream<String, CreditLineFlexFeeOutput5> creditLineFlexFeeOutput5KStream =
                flexCreditLineActivityOutputInfo.join(creditLineUserDetailsOutputInfo, joiner,
                        JoinWindows.of(Duration.ofSeconds(3000)),
                        StreamJoined.with(Serdes.String(), flexCreditLineActivityOutputSerde, creditLineUserDetailsOutputSerde)
                );
        creditLineFlexFeeOutput5KStream.print(Printed.toSysOut());
        creditLineFlexFeeOutput5KStream.foreach(((key, value) ->
                log.info("***** key and value for combined creditLineFlexFeeOutput5KStream ******: :{} :{}", key, value)
        ));

        creditLineFlexFeeOutput5KStream.to(TopicEnum.FLEX_CREDIT_LINE_ACTIVITY_OUT_5_TOPIC.getTopicName(), Produced.with(Serdes.String(), new JsonSerde<>(CreditLineFlexFeeOutput5.class)));
        kStreamConfig.topology(builder);
    }
}
