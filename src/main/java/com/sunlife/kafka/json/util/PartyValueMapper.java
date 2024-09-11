package com.sunlife.kafka.json.util;

import com.sunlife.kafka.json.entity.Phone;
import com.sunlife.kafka.json.model.topic.domain.PhoneValue;

public final class PartyValueMapper {
    public static PhoneValue toPhoneValue(final Phone party){
        final PhoneValue.Builder builder=
                new PhoneValue.Builder()
                        .withPartyId(party.getPartyId())
                        .withCifCreateTimestamp(party.getCifCreateTimeStamp());
        return builder.build();
    }
}
