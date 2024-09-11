package com.sunlife.kafka.json.entity;

import com.sunlife.kafka.json.model.topic.raw.PhoneRecord;

public final class Phone {
    private final PhoneRecord prtyRecord;

    public Phone(PhoneRecord prtyRecord) {
        this.prtyRecord = prtyRecord;
    }

    public Long getPartyId(){
        return prtyRecord.getPartyId();
    }

    public String getFirstName(){
        return prtyRecord.getFirstName();
    }

    public String getCifCreateTimeStamp(){
        return prtyRecord.getCifCreatTmstmp();
    }

    public String getCifUpdateTimestamp(){
        return prtyRecord.getCifUpdtTmstmp();
    }

    public String getAreaCode(){
        return prtyRecord.getPhoneAreaCd();
    }

    public String getPhone(){
        return prtyRecord.getPhoneNumber();
    }
}
