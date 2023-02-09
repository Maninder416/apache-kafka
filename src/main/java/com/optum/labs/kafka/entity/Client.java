package com.optum.labs.kafka.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CLIENTS")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PSGL_DEPARTMENT",length = 10)
    private String psgl_department;
    @Column(name = "BRANCHNBR",length = 5)
    private String branchNbr;
    @Column(name = "CBS_AOTEAMCD",length = 30)
    private String cba_aoteamcd;
    @Column(name ="NAMEADDRLN2",length = 35)
    private String nameAddRln2;
    @Column(name = "NAMEADDRLN3",length = 35)
    private String nameAddRln3;
    @Column(name = "NAMEADDRLN4",length = 35)
    private String nameAddRln4;
    @Column(name = "NAMEADDRLN5",length = 35)
    private String nameAddRln5;
    @Column(name = "NAMEADDRLN6",length = 40)
    private String nameAddRln6;
    @Column(name = "ZIPPOSTALCD",length = 10)
    private String zipPostalCd;
    @Column(name = "FULL_NAME",length = 105)
    private String fullName;
    @Column(name = "STATUSCD",length = 1)
    private String statusCd;
    @Column(name = "EXPIRYDT")
    private Date expiryDate;
    @Column(name = "CIF",length = 18)
    private String cif;
}
