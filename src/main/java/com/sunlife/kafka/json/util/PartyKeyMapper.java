package com.sunlife.kafka.json.util;

import com.sunlife.kafka.json.model.topic.domain.Party;
import com.sunlife.kafka.json.model.topic.domain.PartyKey;

public final class PartyKeyMapper {

    private PartyKeyMapper(){}

    public static PartyKey toPartyKey(final Party party){
        return new PartyKey.Builder().withPartyId(party.getPartyId()).build();
    }

}
