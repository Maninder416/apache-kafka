package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT_DETAILS")
@Data
public class ClientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonProperty("ID")
    private Long id;
    @Column(name = "DEPARTMENT_CODE")
    @JsonProperty("DEPARTMENT_CODE")
    private String departmentCode;
    @Column(name = "BRANCH_NUMBER")
    @JsonProperty("BRANCH_NUMBER")
    private String branchNumber;
    @Column(name = "TEAM_CODE")
    @JsonProperty("TEAM_CODE")
    private String teamCode;
    @Column(name = "CLIENT_NAME")
    @JsonProperty("CLIENT_NAME")
    private String clientName;
    @Column(name = "ADDRESS_LINE1")
    @JsonProperty("ADDRESS_LINE1")
    private String addressLine1;
    @Column(name = "ADDRESS_LINE2")
    @JsonProperty("ADDRESS_LINE2")
    private String addressLine2;
    @Column(name = "ADDRESS_LINE3")
    @JsonProperty("ADDRESS_LINE3")
    private String addressLine3;
    @Column(name = "ADDRESS_LINE4")
    @JsonProperty("ADDRESS_LINE4")
    private String addressLine4;
    @Column(name = "ADDRESS_LINE5")
    @JsonProperty("ADDRESS_LINE5")
    private String addressLine5;
    @Column(name = "ADDRESS_LINE6")
    @JsonProperty("ADDRESS_LINE6")
    private String addressLine6;
    @Column(name = "ZIP_POSTAL_CODE")
    @JsonProperty("ZIP_POSTAL_CODE")
    private String zipPostalCode;

}
