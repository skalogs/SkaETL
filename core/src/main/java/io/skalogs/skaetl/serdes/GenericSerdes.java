package io.skalogs.skaetl.serdes;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class GenericSerdes {

    public static Serde<JsonNode> jsonNodeSerde() {
        return Serdes.serdeFrom(new JsonNodeSerialializer(), new JsonNodeDeserializer());
    }
}
