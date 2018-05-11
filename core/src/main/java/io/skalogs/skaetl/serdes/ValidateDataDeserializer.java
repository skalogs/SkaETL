package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.domain.ValidateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class ValidateDataDeserializer implements Deserializer<ValidateData> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public ValidateData deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return new ValidateData();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ValidateData validateData = null;
        try {
            validateData = mapper.readValue(bytes, ValidateData.class);
        } catch (Exception e) {
            log.error("ValidateDataDeserializer message {}", e);
        }
        return validateData;
    }

    public void close() {

    }
}
