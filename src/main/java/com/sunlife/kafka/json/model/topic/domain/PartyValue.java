package com.sunlife.kafka.json.model.topic.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.annotation.Nullable;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = PartyValue.Builder.class)
public class PartyValue {

    public static final String PARTY_ID_PROPERTY = "party_id";
    public static final String FIRST_NAME_PROPERTY = "first_name";
    public static final String PHONE_NUMBER_PROPERTY = "phone_number";
    public static final String PHONE_AREA_CODE_PROPERTY = "phone_area_code";
    public static final String CIF_CREATE_TIMESTAMP_PROPERTY = "cif_create_timestamp";
    public static final String CIF_UPDATE_TIMESTAMP_PROPERTY = "cif_update_timestamp";

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private @Nullable Long partyId;
        private @Nullable String firstName;
        private @Nullable String phoneNumber;
        private @Nullable String phoneAreaCode;
        private @Nullable String cifCreateTimestamp;
        private @Nullable String cifUpdateTimestamp;

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

        @JsonProperty(PHONE_NUMBER_PROPERTY)
        public Builder withPhoneNumber(final @Nullable String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @JsonProperty(PHONE_AREA_CODE_PROPERTY)
        public Builder withPhoneAreaCode(final @Nullable String phoneAreaCode) {
            this.phoneAreaCode = phoneAreaCode;
            return this;
        }

        @JsonProperty(CIF_CREATE_TIMESTAMP_PROPERTY)
        public Builder withCifCreateTimestamp(final @Nullable String cifCreateTimestamp) {
            this.cifCreateTimestamp = cifCreateTimestamp;
            return this;
        }

        @JsonProperty(CIF_UPDATE_TIMESTAMP_PROPERTY)
        public Builder withCifUpdateTimestamp(final @Nullable String cifUpdateTimestamp) {
            this.cifUpdateTimestamp = cifUpdateTimestamp;
            return this;
        }

        public PartyValue build() {
            return new PartyValue(this);
        }

    }

    private final @Nullable Long partyId;
    private final @Nullable String firstName;
    private final @Nullable String phoneNumber;
    private final @Nullable String phoneAreaCode;
    private final @Nullable String cifCreateTimestamp;
    private final @Nullable String cifUpdateTimestamp;

    private PartyValue(final Builder builder) {
        this.partyId = builder.partyId;
        this.firstName = builder.firstName;
        this.phoneNumber = builder.phoneNumber;
        this.phoneAreaCode = builder.phoneAreaCode;
        this.cifCreateTimestamp = builder.cifCreateTimestamp;
        this.cifUpdateTimestamp = builder.cifUpdateTimestamp;
    }

    @Nullable
    @JsonProperty(value = PARTY_ID_PROPERTY)
    public Long getPartyId() {
        return partyId;
    }

    @Nullable
    @JsonProperty(value = FIRST_NAME_PROPERTY)
    public String getFirstName() {
        return firstName;
    }

    @Nullable
    @JsonProperty(value = PHONE_NUMBER_PROPERTY)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Nullable
    @JsonProperty(value = PHONE_AREA_CODE_PROPERTY)
    public String getPhoneAreaCode() {
        return phoneAreaCode;
    }

    @Nullable
    @JsonProperty(value = CIF_CREATE_TIMESTAMP_PROPERTY)
    public String getCifCreateTimestamp() {
        return cifCreateTimestamp;
    }

    @Nullable
    @JsonProperty(value = CIF_UPDATE_TIMESTAMP_PROPERTY)
    public String getCifUpdateTimestamp() {
        return cifUpdateTimestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                partyId,
                firstName,
                phoneNumber,
                phoneAreaCode,
                cifCreateTimestamp,
                cifUpdateTimestamp);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PartyValue)) return false;

        PartyValue other = (PartyValue) object;
        return Objects.equals(partyId, other.partyId)
                && Objects.equals(firstName, other.firstName)
                && Objects.equals(phoneNumber, other.phoneNumber)
                && Objects.equals(phoneAreaCode, other.phoneAreaCode)
                && Objects.equals(cifCreateTimestamp, other.cifCreateTimestamp)
                && Objects.equals(cifUpdateTimestamp, other.cifUpdateTimestamp);
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
                PHONE_NUMBER_PROPERTY,
                phoneNumber,
                PHONE_AREA_CODE_PROPERTY,
                phoneAreaCode,
                CIF_CREATE_TIMESTAMP_PROPERTY,
                cifCreateTimestamp,
                CIF_UPDATE_TIMESTAMP_PROPERTY,
                cifUpdateTimestamp);
    }

}
