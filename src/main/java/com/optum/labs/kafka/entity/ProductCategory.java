package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_CATEGORY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;

    @Column(name = "ACCTNBR",length = 35)
    @JsonProperty("ACCTNBR")
    private String accountNumber;

    @Column(name = "APPLID",length = 2)
    @JsonProperty("APPLID")
    private String applId;

    @Column(name = "CIF",length = 18)
    @JsonProperty("CIF")
    private String cif;

    @Column(name = "TRANS_CURRENCY_CODE",length = 3)
    @JsonProperty("TRANS_CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "PRODUCT_CD", length = 3)
    @JsonProperty("PRODUCT_CD")
    private String product_cd;
    @Column(name = "PRODUCT_CATEGORY_CD", length = 2)
    @JsonProperty("PRODUCT_CATEGORY_CD")
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
