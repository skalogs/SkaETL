package io.skalogs.skaetl.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.apache.commons.lang3.StringUtils;

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

    public String asJsonPath(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        return "/" + path.replaceAll("\\.", "/");
    }

    public boolean has(JsonNode jsonNode, String path) {
        String jsonPath = asJsonPath(path);
        JsonNode targetNode = jsonNode.at(jsonPath);
        return targetNode != null && targetNode.getNodeType() != JsonNodeType.NULL && targetNode.getNodeType() != JsonNodeType.MISSING;
    }

    public JsonNode at(JsonNode jsonNode, String path) {
        String jsonPath = asJsonPath(path);
        return jsonNode.at(jsonPath);
    }

    public void put(JsonNode jsonNode, String path, JsonNode value) {
        JsonPointer valueNodePointer = JsonPointer.compile(asJsonPath(path));
        JsonPointer parentPointer = valueNodePointer.head();
        addMissingNodeIfNecessary(jsonNode, path);
        JsonNode parentNode = jsonNode.at(parentPointer);

        if (parentNode.getNodeType() == JsonNodeType.OBJECT) {
            ObjectNode parentNodeToUpdate = (ObjectNode) parentNode;
            parentNodeToUpdate.set(valueNodePointer.last().toString().replaceAll("/", ""), value);
        }
    }

    private void addMissingNodeIfNecessary(JsonNode jsonNode, String path) {
        String[] properties = path.split("\\.");
        ObjectNode parent = (ObjectNode) jsonNode;
        for (int i = 0; i < properties.length - 1; i++) {
            String elementName = properties[i];
            JsonNode element = parent.get(elementName);
            if (element == null || element.getNodeType() == JsonNodeType.MISSING || element.getNodeType() == JsonNodeType.NULL) {
                parent.set(elementName, JsonNodeFactory.instance.objectNode());
            }
            parent = (ObjectNode) parent.get(elementName);

        }
    }

    public void remove(JsonNode jsonNode, String path) {
        JsonPointer valueNodePointer = JsonPointer.compile(asJsonPath(path));
        JsonPointer parentPointer = valueNodePointer.head();
        JsonNode parentNode = jsonNode.at(parentPointer);
        if (parentNode.getNodeType() == JsonNodeType.OBJECT) {
            ObjectNode parentNodeToUpdate = (ObjectNode) parentNode;
            parentNodeToUpdate.remove(valueNodePointer.last().toString().replaceAll("/",""));
        }

    }
}
