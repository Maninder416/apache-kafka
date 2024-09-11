package com.sunlife.kafka.json.model.topic.raw;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.annotation.Nullable;

import java.util.Objects;

@JsonDeserialize(builder = PrtyRecord.Builder.class)
public class PrtyRecord {

    public static final String PARTY_ID_PROPERTY = "PARTY_ID";
    public static final String FIRST_NAME_PROPERTY = "FIRST_NAME";
    public static final String PHONE_NUM_PROPERTY = "PHONE_NUM";
    public static final String PHONE_AREA_CD_PROPERTY = "PHONE_AREA_CD";
    public static final String CIF_CREAT_TMSTMP_PROPERTY = "CIF_CREAT_TMSTMP";
    public static final String CIF_UPDT_TMSTMP_PROPERTY = "CIF_UPDT_TMSTMP";

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {

        private @Nullable Long partyId;
        private @Nullable String firstName;
        private @Nullable String phoneNum;
        private @Nullable String phoneAreaCd;
        private @Nullable String cifCreatTmstmp;
        private @Nullable String cifUpdtTmstmp;

        @JsonProperty(PARTY_ID_PROPERTY)
        public Builder withPartyId(final @Nullable Long partyId) {
            this.partyId = partyId;
            return this;
        }

        @JsonProperty(FIRST_NAME_PROPERTY)
        public Builder withFirstName(final @Nullable String firstName) {
            this.firstName = firstName;
            return this;
        }

        @JsonProperty(PHONE_NUM_PROPERTY)
        public Builder withPhoneNum(final @Nullable String phoneNum) {
            this.phoneNum = phoneNum;
            return this;
        }

        @JsonProperty(PHONE_AREA_CD_PROPERTY)
        public Builder withPhoneAreaCd(final @Nullable String phoneAreaCd) {
            this.phoneAreaCd = phoneAreaCd;
            return this;
        }


        @JsonProperty(CIF_CREAT_TMSTMP_PROPERTY)
        public Builder withCifCreatTmstmp(final @Nullable String cifCreatTmstmp) {
            this.cifCreatTmstmp = cifCreatTmstmp;
            return this;
        }

        @JsonProperty(CIF_UPDT_TMSTMP_PROPERTY)
        public Builder withCifUpdtTmstmp(final @Nullable String cifUpdtTmstmp) {
            this.cifUpdtTmstmp = cifUpdtTmstmp;
            return this;
        }

        public PrtyRecord build() {
            return new PrtyRecord(this);
        }
    }

    private final Long partyId;
    private final String firstName;
    private final String phoneNum;
    private final String phoneAreaCd;
    private final String cifCreatTmstmp;
    private final String cifUpdtTmstmp;

    private PrtyRecord(final Builder builder) {
        this.partyId = builder.partyId;
        this.firstName = builder.firstName;
        this.phoneAreaCd = builder.phoneAreaCd;
        ;
        this.phoneNum = builder.phoneNum;
        this.cifCreatTmstmp = builder.cifCreatTmstmp;
        this.cifUpdtTmstmp = builder.cifUpdtTmstmp;
    }

    @JsonProperty(value = PARTY_ID_PROPERTY)
    public Long getPartyId() {
        return partyId;
    }

    @JsonProperty(value = FIRST_NAME_PROPERTY)
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty(value = PHONE_NUM_PROPERTY)
    public String getPhoneNumber() {
        return phoneNum;
    }

    @JsonProperty(value = PHONE_AREA_CD_PROPERTY)
    public String getPhoneAreaCd() {
        return phoneAreaCd;
    }

    @JsonProperty(value = CIF_CREAT_TMSTMP_PROPERTY)
    public String getCifCreatTmstmp() {
        return cifCreatTmstmp;
    }

    @JsonProperty(value = CIF_UPDT_TMSTMP_PROPERTY)
    public String getCifUpdtTmstmp() {
        return cifUpdtTmstmp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                partyId,
                firstName,
                phoneNum,
                phoneAreaCd,
                cifCreatTmstmp,
                cifUpdtTmstmp);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PrtyRecord)) return false;

        PrtyRecord other = (PrtyRecord) object;
        return Objects.equals(partyId, other.partyId)
                && Objects.equals(firstName, other.firstName)
                && Objects.equals(phoneNum, other.phoneNum)
                && Objects.equals(phoneAreaCd, other.phoneAreaCd)
                && Objects.equals(cifCreatTmstmp, other.cifCreatTmstmp)
                && Objects.equals(cifUpdtTmstmp, other.cifUpdtTmstmp);
    }

    @Override
    public String toString() {
        return String.format(
                "%s[%s=%d, %s=%s, %s=%s, %s=%s, %s=%s, %s=%s]",
                this.getClass().getSimpleName(),
                PARTY_ID_PROPERTY,
                partyId,
                FIRST_NAME_PROPERTY,
                firstName,
                PHONE_NUM_PROPERTY,
                phoneNum,
                PHONE_AREA_CD_PROPERTY,
                phoneAreaCd,
                CIF_CREAT_TMSTMP_PROPERTY,
                cifCreatTmstmp,
                CIF_UPDT_TMSTMP_PROPERTY,
                cifUpdtTmstmp);
    }
}
