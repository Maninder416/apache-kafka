package com.optum.labs.kafka.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CLIENTS")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PSGL_DEPARTMENT")
    private String psgl_department;
    @Column(name = "BRANCHNBR")
    private String branchNbr;
    @Column(name = "CBS_AOTEAMCD")
    private String cba_aoteamcd;
    @Column(name ="NAMEADDRLN2")
    private String nameAddRln2;
    @Column(name = "NAMEADDRLN3")
    private String nameAddRln3;
    @Column(name = "NAMEADDRLN4")
    private String nameAddRln4;
    @Column(name = "NAMEADDRLN5")
    private String nameAddRln5;
    @Column(name = "NAMEADDRLN6")
    private String nameAddRln6;
    @Column(name = "ZIPPOSTALCD")
    private String zipPostalCd;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "STATUSCD")
    private String statusCd;
    @Column(name = "EXPIRYDT")
    private Date expiryDate;
    @Column(name = "CIF")
    private String cif;
}
