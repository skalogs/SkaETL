package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenameFieldTransformator extends TransformatorProcess {

    public RenameFieldTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        if (has(parameterTransformation.getComposeField().getKey(), jsonValue)) {
            JsonNode valueField = at(parameterTransformation.getComposeField().getKey(), jsonValue);
            put(jsonValue, parameterTransformation.getComposeField().getValue(), valueField.deepCopy());
            remove(jsonValue, parameterTransformation.getComposeField().getKey());
        }
    }
}