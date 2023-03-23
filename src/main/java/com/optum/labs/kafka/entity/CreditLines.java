package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CREDIT_LINES")
@Data
public class CreditLines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;
    @Column(name = "CUST_LINE_NBR", length = 22)
    @JsonProperty("CUST_LINE_NBR")
    private String custLineNbr;
    @Column(name = "APPLID_LOAN", length = 3)
    @JsonProperty("APPLID_LOAN")
    private String applId_loan;
    @Column(name = "CREDIT_LINE_STATUS", length = 2)
    @JsonProperty("CREDIT_LINE_STATUS")
    private String creditLineStatus;
    @Column(name = "APPLID", length = 3)
    @JsonProperty("APPLID")
    private String applId;
    @Column(name = "POSTDT")
    @JsonProperty("POSTDT")
    private String postDt;
    @Column(name = "CIF", length = 18)
    @JsonProperty("CIF")
    private String cif;
    @Column(name = "TRANS_CRRNCY_CD", length = 3)
    @JsonProperty("TRANS_CURRENCY_CODE")
    private String trans_crrncy_cd;
    @Column(name = "FLEX_FEE_DEBIT_ACC")
    @JsonProperty("FLEX_FEE_DEBIT_ACC")
    private String flex_fee_debit_acc;

}
