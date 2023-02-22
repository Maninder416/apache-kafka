package com.optum.labs.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "BPA_ULF_PRODUCT_CODES")
@Data
@AllArgsConstructor

public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCTNBR",length = 35)
    private String accountNumber;

    @Column(name = "APPLID",length = 2)
    private String applId;

    @Column(name = "CIF",length = 18)
    private String cif;

    @Column(name = "TRANS_CURRENCY_CODE",length = 3)
    private String currencyCode;

    @Column(name = "PRODUCT_CD", length = 3)
    private String product_cd;
    @Column(name = "PRODUCT_CATEGORY_CD", length = 2)
    private String product_category_cd;

    public static class Builder {

        private Long id;
        private String accountNumber;
        private String applId;
        private String cif;
        private String currencyCode;
        private String product_cd;
        private String product_category_cd;

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder applId(String applId) {
            this.applId = applId;
            return this;
        }

        public Builder cif(String cif) {
            this.cif = cif;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder product_cd(String product_cd) {
            this.product_cd = product_cd;
            return this;
        }

        public Builder product_category_cd(String product_category_cd) {
            this.product_category_cd = product_category_cd;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }


        public ProductCategory build() {
            return new ProductCategory(id,accountNumber, applId, cif,currencyCode,product_cd,product_category_cd);
        }
    }
}
