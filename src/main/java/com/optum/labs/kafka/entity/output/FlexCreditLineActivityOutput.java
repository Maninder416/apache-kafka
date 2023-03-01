package com.optum.labs.kafka.entity.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FLEX_CREDIT_LINE_ACTIVITY_OUTPUT")
@Data
public class FlexCreditLineActivityOutput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;

    @Column(name = "EFFDT")
    @Temporal(TemporalType.DATE)
    @JsonProperty("EFFDT")
    private Date effdt;

    @Column(name = "CUST_LN_NBR", length = 22)
    @JsonProperty("CUST_LN_NBR")
    private String custLnNbr;

    @Column(name = "FLEX_CMTMNT_AMT_TCY", precision = 22, scale = 7)
    @JsonProperty("FLEX_CMTMNT_AMT_TCY")
    private String flex_cmtmnt_amt_tcy;

    @Column(name = "FLEX_UNCMTMNT_AMT_TCY", precision = 22, scale = 7)
    @JsonProperty("FLEX_UNCMTMNT_AMT_TCY")
    private String flex_unCmtMnt_amt_tcy;

    @Column(name = "FLEX_FEE_PCT", precision = 7, scale = 4)
    @JsonProperty("FLEX_FEE_PCT")
    private double flex_fee_pct;
    @Column(name = "FLEX_FEE_ACCR_BAS", length = 6)
    @JsonProperty("FLEX_FEE_ACCR_BAS")
    private String flex_fee_accr_bas;
    @Column(name = "TRANS_CRRNCY_CD", length = 3)
    @JsonProperty("TRANS_CRRNCY_CD")
    private String trans_crrncy_cd;
}
