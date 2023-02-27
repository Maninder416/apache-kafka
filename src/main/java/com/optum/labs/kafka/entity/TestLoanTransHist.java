package com.optum.labs.kafka.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TEST_LOAN_TRANS_HIST")
@Data
public class TestLoanTransHist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ACCTNBR", length = 35)
    private String acctNbr;

    @Column(name = "TRANID", length = 30)
    private String tranId;
    @Column(name = "POSTDT")
    @Temporal(TemporalType.DATE)
    private Date postDt;
    @Column(name = "EFFECTIVEDT")
    @Temporal(TemporalType.DATE)
    private Date effectiveDt;
    @Column(name = "NOTEPRNCPLBALGROSS", precision = 19, scale = 3)
    private double notePrncplBalgross;
}
