package com.optum.labs.kafka.entity.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "CURRENCY_LOAN_PRODUCT_CATEGORY_CODE_OUTPUT")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyLoanProductCategoryCodeOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;
    @Column(name = "ACCTNBR", length = 35)
    @JsonProperty("ACCTNBR")
    private String acctNbr;
    @Column(name = "TRANID", length = 30)
    @JsonProperty("TRANID")
    private String tranId;
    @Column(name = "POSTDT")
    @JsonProperty("POSTDT")
    private String postDt;
    @Column(name = "EFFECTIVEDT")
    @JsonProperty("EFFECTIVEDT")
    private String effectiveDt;
    @Column(name = "NOTEPRNCPLBALGROSS", precision = 19, scale = 3)
    @JsonProperty("NOTEPRNCPLBALGROSS")
    private double notePrncplBalgross;
    @Column(name = "TRANS_CURRENCY_CODE", length = 3)
    @JsonProperty("TRANS_CURRENCY_CODE")
    private String currencyCode;
    @Column(name = "PRODUCT_CD", length = 3)
    @JsonProperty("PRODUCT_CD")
    private String product_cd;
    @Column(name = "PRODUCT_CATEGORY_CD", length = 2)
    @JsonProperty("PRODUCT_CATEGORY_CD")
    private String product_category_cd;

}
