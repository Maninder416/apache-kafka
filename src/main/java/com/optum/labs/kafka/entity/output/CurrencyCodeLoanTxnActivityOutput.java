package com.optum.labs.kafka.entity.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


/**
 * In this class, I am not using builder annotation.
 * Just using the default way to create builder for learning
 */
@Entity
@Table(name = "CURRENCY_CODE_LOAN_TXN_OUTPUT")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyCodeLoanTxnActivityOutput {
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

    public static class Builder {
        private Long id;
        private String acctNbr;
        private String tranId;
        private String postDt;
        private String effectiveDt;
        private double notePrncplBalgross;
        private String currencyCode;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setAcctNbr(String acctNbr) {
            this.acctNbr = acctNbr;
            return this;
        }

        public Builder setTranId(String tranId) {
            this.tranId = tranId;
            return this;
        }

        public Builder setPostDt(String postDt) {
            this.postDt = postDt;
            return this;
        }

        public Builder setEffectiveDt(String effectiveDt) {
            this.effectiveDt = effectiveDt;
            return this;
        }

        public Builder setNotePrncplBalgross(double notePrncplBalgross) {
            this.notePrncplBalgross = notePrncplBalgross;
            return this;
        }

        public Builder setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public CurrencyCodeLoanTxnActivityOutput build() {
            return new CurrencyCodeLoanTxnActivityOutput(id, acctNbr, tranId, postDt, effectiveDt, notePrncplBalgross, currencyCode);
        }

    }


}
