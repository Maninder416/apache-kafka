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
    @Column(name = "APPLID")
    private String applId;
    @Column(name = "FACEAMTOFNOTEORGNLBAL_TCY")
    private double faceAmtoFnoteOrgnlbal_tcy;
}
