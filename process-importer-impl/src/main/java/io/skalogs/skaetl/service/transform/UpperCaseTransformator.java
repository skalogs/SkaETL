package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import org.apache.commons.lang.StringUtils;

public class UpperCaseTransformator extends TransformatorProcess {
    public UpperCaseTransformator(TypeValidation type) {
        super(type);
    }

    @Override
    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        if (jsonValue.get(parameterTransformation.getKeyField()) != null &&
                jsonValue.has(parameterTransformation.getKeyField())) {
            JsonNode valueField = jsonValue.path(parameterTransformation.getKeyField());
            String capitalized = StringUtils.upperCase(valueField.textValue());
            jsonValue.put(parameterTransformation.getKeyField(), capitalized);
        }
    }
}
