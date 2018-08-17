package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import org.apache.commons.lang3.StringUtils;

public class TranslateArrayTransformator extends TransformatorProcess {

    public TranslateArrayTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        if (has(parameterTransformation.getKeyField(), jsonValue)) {
            JsonNode jsonNode = at(parameterTransformation.getKeyField(), jsonValue);
            //GeoJSON spec :)
            if (jsonNode.isArray()) {
                for (final JsonNode arrayEntry : jsonNode) {
                    put(jsonValue, StringUtils.lowerCase(arrayEntry.asText()), "true");
                }
            }
        }
    }
}
