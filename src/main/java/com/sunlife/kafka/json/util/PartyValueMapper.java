package com.sunlife.kafka.json.util;

import com.sunlife.kafka.json.model.topic.domain.Party;
import com.sunlife.kafka.json.model.topic.domain.PartyValue;

public final class PartyValueMapper {
    public static PartyValue toPartyValue(final Party party){
        final PartyValue.Builder builder=
                new PartyValue.Builder()
                        .withPartyId(party.getPartyId())
                        .withCifCreateTimestamp(party.getCifCreateTimeStamp());
        return builder.build();
    }
}
