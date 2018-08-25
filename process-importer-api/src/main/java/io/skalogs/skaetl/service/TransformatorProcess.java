package io.skalogs.skaetl.service;

/*-
 * #%L
 * process-importer-api
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


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class TransformatorProcess {

    private final TypeValidation type;

    private final JSONUtils jsonUtils = JSONUtils.getInstance();

    public boolean type(TypeValidation typeValidation) {
        return type.equals(typeValidation);
    }

    public abstract void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue);

    protected boolean has(String path, JsonNode jsonNode) {
        return jsonUtils.has(jsonNode, path);
    }

    protected JsonNode at(String path, JsonNode jsonNode) {
        return jsonUtils.at(jsonNode, path);
    }

    protected void put(JsonNode jsonNode, String path, String value) {
        jsonUtils.put(jsonNode, path, JsonNodeFactory.instance.textNode(value));
    }

    protected void put(JsonNode jsonNode, String path, Boolean value) {
        jsonUtils.put(jsonNode, path, JsonNodeFactory.instance.booleanNode(value));
    }

    protected void put(JsonNode jsonNode, String path, Double value) {
        jsonUtils.put(jsonNode, path, JsonNodeFactory.instance.numberNode(value));
    }

    protected void put(JsonNode jsonNode, String path, Long value) {
        jsonUtils.put(jsonNode, path, JsonNodeFactory.instance.numberNode(value));
    }

    protected void put(JsonNode jsonNode, String path, JsonNode value) {
        jsonUtils.put(jsonNode, path, value);
    }

    protected void remove(JsonNode jsonValue, String path) {
        jsonUtils.remove(jsonValue,path);
    }
}
