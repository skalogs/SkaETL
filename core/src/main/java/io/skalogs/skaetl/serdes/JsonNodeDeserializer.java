package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.utils.JSONUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonNodeDeserializer implements Deserializer<JsonNode> {


    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public JsonNode deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return JSONUtils.getInstance().parse(new String(bytes));
    }

    @Override
    public void close() {

    }
}
