package com.optum.labs.kafka.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "BPA_ULF_PRODUCT_CODES")
@Data
public class BpaUlfProductCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;
    @Column(name = "PRODUCT_CD", length = 3)
    @JsonProperty("PRODUCT_CD")
    private String product_cd;
    @Column(name = "PRODUCT_CATEGORY_CD", length = 2)
    @JsonProperty("PRODUCT_CATEGORY_CD")
    private String product_category_cd;
}
