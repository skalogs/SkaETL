package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.domain.SimulateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class SimulateDataDeserializer implements Deserializer<SimulateData> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public SimulateData deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return new SimulateData();
        }
        ObjectMapper mapper = new ObjectMapper();
        SimulateData simulateData = null;
        try {
            simulateData = mapper.readValue(bytes, SimulateData.class);
        } catch (Exception e) {
            log.error("SimulateDataDeserializer message {}", e);
        }
        return simulateData;
    }

    public void close() {

    }
}
