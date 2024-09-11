package com.sunlife.kafka.json.topology.transformation.map;
import com.sunlife.kafka.json.model.Phone;
import com.sunlife.kafka.json.model.topic.domain.Party;
import com.sunlife.kafka.json.model.topic.domain.PartyKey;
import com.sunlife.kafka.json.model.topic.domain.PartyValue;
import com.sunlife.kafka.json.model.topic.raw.PrtyRecord;
import com.sunlife.kafka.json.util.PartyKeyMapper;
import com.sunlife.kafka.json.util.PartyValueMapper;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;

public class PhoneKeyAndValueMapper implements KeyValueMapper<String, PrtyRecord, KeyValue<PartyKey, PartyValue>> {

    @Override
    public KeyValue<PartyKey, PartyValue> apply(String s, PrtyRecord prtyRecord) {
        final Party party= new Party(prtyRecord);
        final PartyValue partyValue= PartyValueMapper.toPartyValue(party);
        final PartyKey partyKey= PartyKeyMapper.toPartyKey(party);
        return KeyValue.pair(partyKey,partyValue);
    }
}
