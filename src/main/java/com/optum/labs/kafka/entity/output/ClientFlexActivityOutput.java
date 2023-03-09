package com.optum.labs.kafka.entity.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CLIENT_FLEX_ACTIVITY_OUTPUT")
@AllArgsConstructor
@NoArgsConstructor
public class ClientFlexActivityOutput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;

    @Column(name = "CUST_LINE_NBR")
    @JsonProperty("CUST_LINE_NBR")
    private String customerLineNumber;

    //    @Column(name = "POSTDT")
//    @JsonProperty("POSTDT")
//    @Temporal(TemporalType.DATE)
//    private Date postDt;
    @Column(name = "POSTDT")
    @JsonProperty("POSTDT")
    private String postDt;

    @Column(name = "PSGL_DEPARTMENT", length = 10)
    @JsonProperty("PSGL_DEPARTMENT")
    private String psgl_department;
    @Column(name = "BRANCHNBR", length = 5)
    @JsonProperty("BRANCHNBR")
    private String branchNbr;
    @Column(name = "CBS_AOTEAMCD", length = 30)
    @JsonProperty("CBS_AOTEAMCD")
    private String cba_aoteamcd;
    @Column(name = "NAMEADDRLN2", length = 35)
    @JsonProperty("NAMEADDRLN2")
    private String nameAddRln2;
    @Column(name = "NAMEADDRLN3", length = 35)
    @JsonProperty("NAMEADDRLN3")
    private String nameAddRln3;
    @Column(name = "NAMEADDRLN4", length = 35)
    @JsonProperty("NAMEADDRLN4")
    private String nameAddRln4;
    @Column(name = "NAMEADDRLN5", length = 35)
    @JsonProperty("NAMEADDRLN5")
    private String nameAddRln5;
    @Column(name = "NAMEADDRLN6", length = 40)
    @JsonProperty("NAMEADDRLN6")
    private String nameAddRln6;
    @Column(name = "ZIPPOSTALCD", length = 10)
    @JsonProperty("ZIPPOSTALCD")
    private String zipPostalCd;
    @Column(name = "FULL_NAME", length = 105)
    @JsonProperty("FULL_NAME")
    private String fullName;

    public static class Builder {
        private Long id;
        private String customerLineNumber;
//        private Date postDt;
private String postDt;

        private String psgl_department;
        private String branchNbr;
        private String cba_aoteamcd;
        private String nameAddRln2;
        private String nameAddRln3;
        private String nameAddRln4;
        private String nameAddRln5;
        private String nameAddRln6;
        private String zipPostalCd;
        private String fullName;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setCustomerLineNumber(String customerLineNumber) {
            this.customerLineNumber = customerLineNumber;
            return this;
        }

//        public Builder setPostDt(Date postDt) {
//            this.postDt = postDt;
//            return this;
//        }

        public Builder setPostDt(String postDt) {
            this.postDt = postDt;
            return this;
        }

        public Builder setPsgl_department(String psgl_department) {
            this.psgl_department = psgl_department;
            return this;
        }

        public Builder setBranchNbr(String branchNbr) {
            this.branchNbr = branchNbr;
            return this;
        }

        public Builder setCba_aoteamcd(String cba_aoteamcd) {
            this.cba_aoteamcd = cba_aoteamcd;
            return this;
        }

        public Builder setNameAddRln2(String nameAddRln2) {
            this.nameAddRln2 = nameAddRln2;
            return this;
        }

        public Builder setNameAddRln3(String nameAddRln3) {
            this.nameAddRln3 = nameAddRln3;
            return this;
        }

        public Builder setNameAddRln4(String nameAddRln4) {
            this.nameAddRln4 = nameAddRln4;
            return this;
        }

        public Builder setNameAddRln5(String nameAddRln5) {
            this.nameAddRln5 = nameAddRln5;
            return this;
        }

        public Builder setNameAddRln6(String nameAddRln6) {
            this.nameAddRln6 = nameAddRln6;
            return this;
        }

        public Builder setZipPostalCd(String zipPostalCd) {
            this.zipPostalCd = zipPostalCd;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public ClientFlexActivityOutput build() {
            return new ClientFlexActivityOutput(id, customerLineNumber, postDt, psgl_department, branchNbr,
                    cba_aoteamcd, nameAddRln2, nameAddRln3, nameAddRln4, nameAddRln5, nameAddRln6, zipPostalCd, fullName);
        }


    }
}
