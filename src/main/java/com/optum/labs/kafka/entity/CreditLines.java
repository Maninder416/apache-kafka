package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    @Temporal(TemporalType.DATE)
    private Date postDt;

}
