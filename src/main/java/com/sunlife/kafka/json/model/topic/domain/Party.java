package com.sunlife.kafka.json.model.topic.domain;

import com.sunlife.kafka.json.model.topic.raw.PrtyRecord;

public final class Party {
    private final PrtyRecord prtyRecord;

    public Party(PrtyRecord prtyRecord) {
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
