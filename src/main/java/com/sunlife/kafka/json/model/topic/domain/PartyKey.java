package com.sunlife.kafka.json.model.topic.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.annotation.Nullable;

import java.util.Objects;

@JsonDeserialize(builder = PartyKey.Builder.class)
public final class PartyKey {

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
  public static class Builder {

    private @Nullable Long partyId;

    @JsonProperty(PartyValue.PARTY_ID_PROPERTY)
    public Builder withPartyId(final Long partyId) {
      this.partyId = partyId;
      return this;
    }

    public PartyKey build() {
      return new PartyKey(this);
    }
  }

  private final @Nullable Long partyId;

  private PartyKey(final Builder builder) {
    this.partyId = builder.partyId;
  }

  @Nullable
  @JsonProperty(value = PartyValue.PARTY_ID_PROPERTY, required = true)
  public Long getPartyId() {
    return partyId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof PartyKey)) return false;

    PartyKey other = (PartyKey) object;
    return Objects.equals(partyId, other.partyId);
  }

  @Override
  public String toString() {
    return String.format(
        "%s[%s=%d]",
        this.getClass().getSimpleName(), PartyValue.PARTY_ID_PROPERTY, partyId);
  }
}
