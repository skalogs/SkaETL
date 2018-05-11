package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.domain.ValidateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class ValidateDataSerializer implements Serializer<ValidateData> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, ValidateData validateData) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(validateData).getBytes();
        } catch (Exception e) {
            log.error("ValidateDataSerializer source {} message {}", validateData, e);
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}