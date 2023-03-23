package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This class, I have prepared for testing purpose as there is alot
 * of calculation that I need to do. So, to check the basic calculation
 * whether it is working or not, I have created this class.
 */

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
