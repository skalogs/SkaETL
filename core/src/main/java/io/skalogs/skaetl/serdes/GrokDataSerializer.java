package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.domain.GrokData;
import io.skalogs.skaetl.domain.ValidateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class GrokDataSerializer implements Serializer<GrokData> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, GrokData grokData) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(grokData).getBytes();
        } catch (Exception e) {
            log.error("GrokDataSerializer source {} message {}", grokData, e);
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}