package com.optum.labs.kafka.entity;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "LOANS_FACT_VW")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ACCTNBR")
    private Integer accountNumber;
    @Column(name = "ACCTNBR_CREDIT_LINE")
    private Integer accountNumberCreditLine;
    @Column(name = "APPLID",length = 2)
    private String applId;
    @Column(name = "FACEAMTOFNOTEORGNLBAL_TCY",precision = 19,scale = 4)
    private double faceAmtoFnoteOrgnlbal_tcy;
}
