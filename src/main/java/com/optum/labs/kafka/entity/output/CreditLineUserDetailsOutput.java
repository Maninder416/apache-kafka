package com.optum.labs.kafka.entity.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CREDIT_LINE_USER_DETAILS_OUTPUT")
@Data
@Builder
public class CreditLineUserDetailsOutput {

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

    @Column(name = "PSGL_DEPARTMENT", length = 10)
    @JsonProperty("PSGL_DEPARTMENT")
    private String psgl_department;
    @Column(name = "BRANCHNBR", length = 5)
    @JsonProperty("BRANCHNBR")
    private String branchNbr;
    @Column(name = "CBS_AOTEAMCD", length = 30)
    @JsonProperty("CBS_AOTEAMCD")
    private String cba_aoteamcd;
    @Column(name = "NAMEADDRLN1", length = 35)
    @JsonProperty("NAMEADDRLN1")
    private String nameAddRln1;
    @Column(name = "NAMEADDRLN2", length = 35)
    @JsonProperty("NAMEADDRLN2")
    private String nameAddRln2;
    @Column(name = "NAMEADDRLN3", length = 35)
    @JsonProperty("NAMEADDRLN3")
    private String nameAddRln3;
    @Column(name = "NAMEADDRLN4", length = 35)
    @JsonProperty("NAMEADDRLN4")
    private String nameAddRln4;
    @Column(name = "NAMEADDRLN5", length = 35)
    @JsonProperty("NAMEADDRLN5")
    private String nameAddRln5;
    @Column(name = "NAMEADDRLN6", length = 40)
    @JsonProperty("NAMEADDRLN6")
    private String nameAddRln6;
    @Column(name = "ZIPPOSTALCD", length = 10)
    @JsonProperty("ZIPPOSTALCD")
    private String zipPostalCd;
    @Column(name = "FULL_NAME", length = 105)
    @JsonProperty("FULL_NAME")
    private String fullName;
    @Column(name = "STATUSCD", length = 1)
    @JsonProperty("STATUSCD")
    private String statusCd;
    @Column(name = "EXPIRYDT")
    @JsonProperty("EXPIRYDT")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    @Column(name = "CIF", length = 18)
    @JsonProperty("CIF")
    private String cif;

}
