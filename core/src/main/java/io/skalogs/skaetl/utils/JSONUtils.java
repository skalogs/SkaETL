package io.skalogs.skaetl.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.io.IOException;

public class JSONUtils {

    private final ObjectMapper objectMapper;
    private static JSONUtils INSTANCE = new JSONUtils();

    private JSONUtils() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    public static JSONUtils getInstance() {
        return INSTANCE;
    }

    public JsonNode parse(String raw) {
        try {
            return objectMapper.readTree(raw);
        } catch (IOException e) {
            return null;
        }
    }

    public ObjectNode parseObj(String raw) {
        return (ObjectNode) parse(raw);
    }

    public <T> String asJsonString(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public <T> T parse(String raw, Class<T> destClass) {
        try {
            return objectMapper.readValue(raw, destClass);
        } catch (IOException e) {
            return null;
        }
    }


    public JsonNode toJsonNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }
}
