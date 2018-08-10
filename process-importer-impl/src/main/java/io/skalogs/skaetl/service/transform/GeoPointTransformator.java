package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeoPointTransformator extends TransformatorProcess {

    public GeoPointTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        if (has(parameterTransformation.getKeyField(),jsonValue)) {
            JsonNode jsonNode = at(parameterTransformation.getKeyField(), jsonValue);
            put(jsonValue, parameterTransformation.getKeyField() + "_gp", jsonNode);
            remove(jsonValue,parameterTransformation.getKeyField());
        }
    }
}
