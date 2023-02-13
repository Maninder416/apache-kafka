package com.optum.labs.kafka.entity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "BPA_ULF_PRODUCT_CODES")
@Data
public class BpaUlfProductCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PRODUCT_CD", length = 3)
    private String product_cd;
    @Column(name = "PRODUCT_CATEGORY_CD", length = 2)
    private String product_category_cd;
}
