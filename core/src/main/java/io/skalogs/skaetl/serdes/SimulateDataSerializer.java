package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.domain.SimulateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class SimulateDataSerializer implements Serializer<SimulateData> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, SimulateData simulateData) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String toto = objectMapper.writeValueAsString(simulateData);
            retVal = toto.getBytes();
        } catch (Exception e) {
            log.error("SimulateDataSerializer source {} message {}", simulateData, e);
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}