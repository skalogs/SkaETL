package io.skalogs.skaetl.utils;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    public static JSONUtils getInstance() {
        return INSTANCE;
    }

    public JsonNode parse(String raw) {
        try {
            return parseWithError(raw);
        } catch (IOException e) {
            return null;
        }
    }

    public JsonNode parseWithError(String raw) throws IOException {
            return objectMapper.readTree(raw);
    }

    public ObjectNode parseObj(String raw) {
        return (ObjectNode) parse(raw);
    }

    public <T> String asJsonString(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public <T> T parse(byte[] raw, Class<T> destClass) {
        try {
            return objectMapper.readValue(raw, destClass);
        } catch (IOException e) {
            return null;
        }
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
        return "/" + StringUtils.replace(path,".", "/");
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
            parentNodeToUpdate.set(StringUtils.replace(valueNodePointer.last().toString(),"/", ""), value);
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
            parentNodeToUpdate.remove(StringUtils.replace(valueNodePointer.last().toString(),"/",""));
        }

    }
}
