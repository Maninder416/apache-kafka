package com.sunlife.kafka.json.util;

import com.sunlife.kafka.json.entity.Phone;
import com.sunlife.kafka.json.model.topic.domain.PhoneKey;

public final class PhoneKeyMapper {

    private PhoneKeyMapper(){}

    public static PhoneKey toPhoneKey(final Phone phone){
        return new PhoneKey.Builder().withPartyId(phone.getPartyId()).build();
    }

}
