package com.sunlife.kafka.json.topology;

import com.sunlife.kafka.json.config.KStreamConfig;
import com.sunlife.kafka.json.model.Phone;
import com.sunlife.kafka.json.model.topic.domain.PartyKey;
import com.sunlife.kafka.json.model.topic.domain.PartyValue;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.kafka.streams.StreamsBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Slf4j
@Service
public class PhoneTopologyWrapper {

    @Autowired
    private KStreamConfig kStreamConfig;

    public void creditLineDetails() {
        System.out.println("inside this method");
        // Define the topics for input and output
        String phoneInputTopic = "dev.ca.cif.party-phone.raw.0";
        String partyOutputTopic = "dev.ca.cif.party-output";

        StreamsBuilder builder = new StreamsBuilder();
        final Serde<Phone> phoneSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Phone.class));
        final Serde<PartyKey> partyKeySerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(PartyKey.class));
        final Serde<PartyValue> partyValueSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(PartyValue.class));
        KStream<String, Phone> phoneKStream = builder.stream(phoneInputTopic, Consumed.with(Serdes.String(), phoneSerde));
        phoneKStream.print(Printed.toSysOut());
        phoneKStream.foreach((key, value) ->
                System.out.println("***** key value for Phone: "+ key+" : "+value)
        );
        System.out.println("***** phone data ******");
        // Perform some transformation or processing
        // Example: Mapping Phone to PartyKey and PartyValue
        KStream<PartyKey, PartyValue> processedStream = phoneKStream.map(
                (key, phone) -> {
                    PartyKey partyKey = new PartyKey.Builder().withPartyId(phone.getPartyId()).build(); // Example transformation
                    PartyValue partyValue = new PartyValue.Builder()
                            .withPartyId(phone.getPartyId())
                            .withCifCreateTimestamp(phone.getCifCreatTmstmp())
                            .withFirstName(phone.getFirstName())
                            .withCifUpdateTimestamp(phone.getCifUpdtTmstmp())
                            .withPhoneAreaCode(phone.getPhoneAreaCd())
                            .withPhoneNumber(phone.getPhoneNum())
                            .build();
                    return KeyValue.pair(partyKey, partyValue);
                }
        );
        processedStream.foreach((key, value) ->
                System.out.println("***** processedStream key value for Phone: "+ key+" : "+value)
        );
        processedStream.print(Printed.toSysOut());
        processedStream.to(partyOutputTopic, Produced.with(partyKeySerde, partyValueSerde));



        kStreamConfig.topology(builder);
    }
}
