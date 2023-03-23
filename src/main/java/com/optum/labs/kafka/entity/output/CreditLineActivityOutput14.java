package com.optum.labs.kafka.entity.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "CREDIT_LINE_FLEX_CREDITLINE_ACTIVITY_OUT14")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditLineActivityOutput14 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;
    @Column(name = "CUST_LINE_NBR")
    @JsonProperty("CUST_LINE_NBR")
    private String customerLineNumber;
    @Column(name = "POSTDT")
    @JsonProperty("POSTDT")
    private String postDt;
    @Column(name = "CIF", length = 9)
    @JsonProperty("CIF")
    private String cif;
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
    @Column(name = "SRC_UPDT_DT")
    @JsonProperty("SRC_UPDT_DT")
    private String src_updt_dt;
    @Column(name = "DW_CREATE_TS")
    @JsonProperty("DW_CREATE_TS")
    private String dw_create_ts;
    @Column(name = "CREATED_BY", length = 30)
    @JsonProperty("CREATED_BY")
    private String created_by;
    @Column(name = "APPLID_LOAN", length = 3)
    @JsonProperty("APPLID_LOAN")
    private String applId_loan;

    @Column(name = "CREDIT_LINE_STATUS", length = 2)
    @JsonProperty("CREDIT_LINE_STATUS")
    private String creditLineStatus;

    @Column(name = "PSGL_DEPARTMENT", length = 10)
    @JsonProperty("PSGL_DEPARTMENT")
    private String psgl_department;
    @Column(name = "BRANCHNBR", length = 5)
    @JsonProperty("BRANCHNBR")
    private String branchNbr;
    @Column(name = "CBS_AOTEAMCD", length = 30)
    @JsonProperty("CBS_AOTEAMCD")
    private String cba_aoteamcd;
    @Column(name = "NAMEADDRLN1", length = 35)
    @JsonProperty("NAMEADDRLN1")
    private String nameAddRln1;
    @Column(name = "NAMEADDRLN2", length = 35)
    @JsonProperty("NAMEADDRLN2")
    private String nameAddRln2;
    @Column(name = "NAMEADDRLN3", length = 35)
    @JsonProperty("NAMEADDRLN3")
    private String nameAddRln3;
    @Column(name = "NAMEADDRLN4", length = 35)
    @JsonProperty("NAMEADDRLN4")
    private String nameAddRln4;
    @Column(name = "NAMEADDRLN5", length = 35)
    @JsonProperty("NAMEADDRLN5")
    private String nameAddRln5;
    @Column(name = "NAMEADDRLN6", length = 40)
    @JsonProperty("NAMEADDRLN6")
    private String nameAddRln6;
    @Column(name = "ZIPPOSTALCD", length = 10)
    @JsonProperty("ZIPPOSTALCD")
    private String zipPostalCd;
    @Column(name = "FULL_NAME", length = 105)
    @JsonProperty("FULL_NAME")
    private String fullName;
    @Column(name = "STATUSCD", length = 1)
    @JsonProperty("STATUSCD")
    private String statusCd;
    @Column(name = "EXPIRYDT")
    @JsonProperty("EXPIRYDT")
    private String expiryDate;
}
