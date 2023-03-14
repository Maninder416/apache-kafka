package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CanDelete")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CanDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonProperty("ID")
    private Long id;

    @Column(name = "POSTDATE")
    @JsonProperty("POSTDATE")
    private String postDate;
    @Column(name = "EFFECTIVEDATE")
    @JsonProperty("EFFECTIVEDATE")
    private String effectiveDate;
    @Column(name = "AMOUNT")
    @JsonProperty("AMOUNT")
    private double amount;
    @Column(name = "ACCOUNTBALANCE")
    @JsonProperty("ACCOUNTBALANCE")
    private double accountBalance;
}
