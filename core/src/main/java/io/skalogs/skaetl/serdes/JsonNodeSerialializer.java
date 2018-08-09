package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonNodeSerialializer implements Serializer<JsonNode> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, JsonNode jsonNode) {
        byte[] retVal = null;
        try {
            retVal = jsonNode.toString().getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
