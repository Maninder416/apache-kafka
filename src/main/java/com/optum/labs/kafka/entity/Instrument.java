package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "INSTRUMENT")
@Data
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;
    @Column(name = "ACCTNBR",length = 35)
    @JsonProperty("ACCTNBR")
    private String accountNumber;

    @Column(name = "APPLID",length = 2)
    @JsonProperty("APPLID")
    private String applId;

    @Column(name = "CIF",length = 18)
    @JsonProperty("CIF")
    private String cif;

    @Column(name = "PRODUCT_CD",length = 3)
    @JsonProperty("PRODUCT_CD")
    private String prodCd;

    @Column(name = "TRANS_CURRENCY_CODE",length = 3)
    @JsonProperty("TRANS_CURRENCY_CODE")
    private String currencyCode;
}
