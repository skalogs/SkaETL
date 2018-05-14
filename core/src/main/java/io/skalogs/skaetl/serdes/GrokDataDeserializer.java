package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.domain.GrokData;
import io.skalogs.skaetl.domain.ValidateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class GrokDataDeserializer implements Deserializer<GrokData> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public GrokData deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return new GrokData();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GrokData grokData = null;
        try {
            grokData = mapper.readValue(bytes, GrokData.class);
        } catch (Exception e) {
            log.error("GrokDataDeserializer message {}", e);
        }
        return grokData;
    }

    public void close() {

    }
}
