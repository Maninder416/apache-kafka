package com.optum.labs.kafka.entity;

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
    private Long id;

    @Column(name = "CIF", length = 9)
    private String cif;
    @Column(name = "CUST_LN_NBR", length = 22)
    private String custLnNbr;
    @Column(name = "EFFDT")
    @Temporal(TemporalType.DATE)
    private Date effdt;
    @Column(name = "FLEX_CMTMNT_AMT_LCY", precision = 22, scale = 7)
    private double flex_cmtmnt_amt_lcy;
    @Column(name = "FLEX_CMTMNT_AMT_TCY", precision = 22, scale = 7)
    private String flex_cmtmnt_amt_tcy;
    @Column(name = "FLEX_UNCMTMNT_AMT_LCY", precision = 22, scale = 7)
    private String flex_unCmtMnt_amt_lcy;
    @Column(name = "FLEX_UNCMTMNT_AMT_TCY", precision = 22, scale = 7)
    private String flex_unCmtMnt_amt_tcy;
    @Column(name = "FLEX_FEE_PCT", precision = 7, scale = 4)
    private double flex_fee_pct;
    @Column(name = "FLEX_FEE_ACCR_BAS", length = 6)
    private String flex_fee_accr_bas;
    @Column(name = "TRANS_CRRNCY_CD", length = 3)
    private String trans_crrncy_cd;
    @Column(name = "ENTITY", length = 3)
    private String entity;
    @Column(name = "APPLID", length = 3)
    private String applId;
    @Column(name = "SRC_UPDT_DT")
    @Temporal(TemporalType.DATE)
    private Date src_updt_dt;
    @Column(name = "POSTDT")
    @Temporal(TemporalType.DATE)
    private Date postDt;
    @Column(name = "DW_CREATE_TS")
    @Temporal(TemporalType.DATE)
    private Date dw_create_ts;
    @Column(name = "CREATED_BY", length = 30)
    private String created_by;
}
