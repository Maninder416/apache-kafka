package com.sunlife.kafka.json.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Phone")
@Data
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    @JsonProperty("PARTY_ID")
    private Long partyId;
    @Column(name = "first_name")
    @JsonProperty("FIRST_NAME")
    private String firstName;
    @Column(name = "phone_num")
    @JsonProperty("PHONE_NUM")
    private String phoneNum;
    @Column(name = "phone_area_cd")
    @JsonProperty("PHONE_AREA_CD")
    private String phoneAreaCd;
    @Column(name = "cif_creat_tmstmp")
    @JsonProperty("CIF_CREAT_TMSTMP")
    private String cifCreatTmstmp;
    @Column(name = "cif_updt_tmstmp")
    @JsonProperty("CIF_UPDT_TMSTMP")
    private String cifUpdtTmstmp;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneAreaCd() {
        return phoneAreaCd;
    }

    public void setPhoneAreaCd(String phoneAreaCd) {
        this.phoneAreaCd = phoneAreaCd;
    }

    public String getCifCreatTmstmp() {
        return cifCreatTmstmp;
    }

    public void setCifCreatTmstmp(String cifCreatTmstmp) {
        this.cifCreatTmstmp = cifCreatTmstmp;
    }

    public String getCifUpdtTmstmp() {
        return cifUpdtTmstmp;
    }

    public void setCifUpdtTmstmp(String cifUpdtTmstmp) {
        this.cifUpdtTmstmp = cifUpdtTmstmp;
    }
}
