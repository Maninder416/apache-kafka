package com.optum.labs.kafka.entity;
import javax.persistence.*;

@Entity
@Table(name = "BPA_ULF_PRODUCT_CODES")
public class BpaUlfProductCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PRODUCT_CD", length = 3)
    private String product_cd;
    @Column(name = "PRODUCT_CATEGORY_CD", length = 2)
    private String product_category_cd;
}
