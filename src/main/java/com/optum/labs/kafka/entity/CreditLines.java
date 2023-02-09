package com.optum.labs.kafka.entity;

import javax.persistence.*;

@Entity
@Table(name = "CREDIT_LINES")
public class CreditLines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUST_LINE_NBR", length = 22)
    private String custLineNbr;

    @Column(name = "APPLID_LOAN", length = 3)
    private String applId_loan;

    @Column(name = "LINE_STAT",length = 2)
    private String line_stat;
}
