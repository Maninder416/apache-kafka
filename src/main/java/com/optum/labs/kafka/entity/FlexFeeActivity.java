package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "FLEX_FEE_ACTIVITY")
@Data
public class FlexFeeActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;

    @Column(name = "CIF", length = 9)
    @JsonProperty("CIF")
    private String cif;
    @Column(name = "CUST_LINE_NBR", length = 22)
    @JsonProperty("CUST_LINE_NBR")
    private String custLnNbr;
//    @Column(name = "EFFDT")
//    @Temporal(TemporalType.DATE)
//    @JsonProperty("EFFDT")
//    private Date effdt;

    @Column(name = "EFFDT")
    @JsonProperty("EFFDT")
    private String effdt;
    @Column(name = "FLEX_CMTMNT_AMT_LCY", precision = 22, scale = 7)
    @JsonProperty("FLEX_CMTMNT_AMT_LCY")
    private double flex_cmtmnt_amt_lcy;
    @Column(name = "FLEX_CMTMNT_AMT_TCY", precision = 22, scale = 7)
    @JsonProperty("FLEX_CMTMNT_AMT_TCY")
    private String flex_cmtmnt_amt_tcy;
    @Column(name = "FLEX_UNCMTMNT_AMT_LCY", precision = 22, scale = 7)
    @JsonProperty("FLEX_UNCMTMNT_AMT_LCY")
    private String flex_unCmtMnt_amt_lcy;
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
    @Column(name = "ENTITY", length = 3)
    @JsonProperty("ENTITY")
    private String entity;
    @Column(name = "APPLID", length = 3)
    @JsonProperty("APPLID")
    private String applId;
//    @Column(name = "SRC_UPDT_DT")
//    @Temporal(TemporalType.DATE)
//    @JsonProperty("SRC_UPDT_DT")
//    private Date src_updt_dt;
//    @Column(name = "POSTDT")
//    @Temporal(TemporalType.DATE)
//    @JsonProperty("POSTDT")
//    private Date postDt;
//    @Column(name = "DW_CREATE_TS")
//    @Temporal(TemporalType.DATE)
//    @JsonProperty("DW_CREATE_TS")
//    private Date dw_create_ts;

    @Column(name = "SRC_UPDT_DT")
    @JsonProperty("SRC_UPDT_DT")
    private String src_updt_dt;
    @Column(name = "POSTDT")
    @JsonProperty("POSTDT")
    private String postDt;
    @Column(name = "DW_CREATE_TS")
    @JsonProperty("DW_CREATE_TS")
    private String dw_create_ts;
    @Column(name = "CREATED_BY", length = 30)
    @JsonProperty("CREATED_BY")
    private String created_by;
}
