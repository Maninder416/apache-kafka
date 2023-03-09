package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TEST_LOAN_TRANS_HIST")
@Data
public class TestLoanTransHist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;
    @Column(name = "ACCTNBR", length = 35)
    @JsonProperty("ACCTNBR")
    private String acctNbr;

    @Column(name = "TRANID", length = 30)
    @JsonProperty("TRANID")
    private String tranId;
//    @Column(name = "POSTDT")
//    @JsonProperty("POSTDT")
//    @Temporal(TemporalType.DATE)
//    private Date postDt;
//    @Column(name = "EFFECTIVEDT")
//    @JsonProperty("EFFECTIVEDT")
//    @Temporal(TemporalType.DATE)
//    private Date effectiveDt;

    @Column(name = "POSTDT")
    @JsonProperty("POSTDT")
    private String postDt;
    @Column(name = "EFFECTIVEDT")
    @JsonProperty("EFFECTIVEDT")
    private String effectiveDt;
    @Column(name = "NOTEPRNCPLBALGROSS", precision = 19, scale = 3)
    @JsonProperty("NOTEPRNCPLBALGROSS")
    private double notePrncplBalgross;
}
