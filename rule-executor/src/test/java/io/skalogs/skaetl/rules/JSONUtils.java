package io.skalogs.skaetl.rules;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtils {
    public static JsonNode createJsonNode(String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(value);
        } catch (IOException e) {
            return null;
        }
    }
}
