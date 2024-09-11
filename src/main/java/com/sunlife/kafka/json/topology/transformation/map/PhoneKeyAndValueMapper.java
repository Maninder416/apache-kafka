package com.sunlife.kafka.json.topology.transformation.map;
import com.sunlife.kafka.json.entity.Phone;
import com.sunlife.kafka.json.model.topic.domain.PhoneKey;
import com.sunlife.kafka.json.model.topic.domain.PhoneValue;
import com.sunlife.kafka.json.model.topic.raw.PhoneRecord;
import com.sunlife.kafka.json.util.PhoneKeyMapper;
import com.sunlife.kafka.json.util.PartyValueMapper;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;

public class PhoneKeyAndValueMapper implements KeyValueMapper<String, PhoneRecord, KeyValue<PhoneKey, PhoneValue>> {

    @Override
    public KeyValue<PhoneKey, PhoneValue> apply(String s, PhoneRecord phoneRecord) {
        final Phone party= new Phone(phoneRecord);
        final PhoneValue phoneValue= PartyValueMapper.toPhoneValue(party);
        final PhoneKey phoneKey= PhoneKeyMapper.toPhoneKey(party);
        return KeyValue.pair(phoneKey,phoneValue);
    }
}
