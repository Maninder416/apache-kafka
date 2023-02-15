package com.optum.labs.kafka.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "INSTRUMENT")
@Data
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCTNBR",length = 35)
    private String accountNumber;

    @Column(name = "APPLID",length = 2)
    private String applId;

    @Column(name = "CIF",length = 18)
    private String cif;

    @Column(name = "PRODCD",length = 3)
    private String prodCd;

    @Column(name = "TRANS_CURRENCY_CODE",length = 3)
    private String currencyCode;
}
