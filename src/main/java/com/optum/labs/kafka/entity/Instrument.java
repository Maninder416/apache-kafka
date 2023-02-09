package com.optum.labs.kafka.entity;

import javax.persistence.*;

@Entity
@Table(name = "INSTRUMENT")
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCTNBR")
    private String accountNumber;

    @Column(name = "APPLID")
    private String applId;

    @Column(name = "CIF")
    private String cif;

    @Column(name = "TRANS_CURRENCY_CODE")
    private String currencyCode;
}
