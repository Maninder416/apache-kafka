package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "CanDelete")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CanDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonProperty("ID")
    private Long id;

    @Column(name = "POSTDATE")
    @JsonProperty("POSTDATE")
    private String postDate;
    @Column(name = "EFFECTIVEDATE")
    @JsonProperty("EFFECTIVEDATE")
    private String effectiveDate;
    @Column(name = "AMOUNT")
    @JsonProperty("AMOUNT")
    private double amount;
    @Column(name = "ACCOUNTBALANCE")
    @JsonProperty("ACCOUNTBALANCE")
    private double accountBalance;

    @Override
    public int hashCode() {
        return postDate.hashCode();
//        return Objects.hash(postDate, effectiveDate,amount,accountBalance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CanDelete other = (CanDelete) obj;
        return postDate== other.postDate;
//        return Objects.equals(postDate, other.postDate) && Objects.equals(effectiveDate, other.effectiveDate)
//                && Objects.equals(amount, other.getAmount()) &&
//                Objects.equals(accountBalance,other.getAccountBalance());
    }
}
