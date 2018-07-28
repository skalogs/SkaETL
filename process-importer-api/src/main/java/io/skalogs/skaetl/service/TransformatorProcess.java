package io.skalogs.skaetl.service;


import com.fasterxml.jackson.databind.JsonNode;
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

    public Boolean type(TypeValidation typeValidation) {
        return type.equals(typeValidation);
    }

    public abstract void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value);

    protected boolean has(String path, JsonNode jsonNode) {
        return jsonUtils.has(path,jsonNode);
    }

    protected JsonNode at(String path, JsonNode jsonNode) {
        return jsonUtils.at(path,jsonNode);
    }

    protected void put(String path, JsonNode jsonNode, String value) {
        jsonUtils.put(path,jsonNode,value);
    }

    protected void put(String path, JsonNode jsonNode, Boolean value) {
        jsonUtils.put(path,jsonNode,value);
    }

    protected void put(String path, JsonNode jsonNode, Double value) {
        jsonUtils.put(path,jsonNode,value);
    }

    protected void put(String path, JsonNode jsonNode, Long value) {
        jsonUtils.put(path,jsonNode,value);
    }

    protected void put(String path, JsonNode jsonNode, JsonNode value) {
        jsonUtils.put(path,jsonNode,value);
    }
}
