package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import org.apache.commons.lang.StringUtils;

public class SwapCaseTransformator extends TransformatorProcess {
    public SwapCaseTransformator(TypeValidation type) {
        super(type);
    }

    @Override
    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        if (has(parameterTransformation.getKeyField(), jsonValue)) {
            JsonNode valueField = at(parameterTransformation.getKeyField(), jsonValue);
            String capitalized = StringUtils.swapCase(valueField.textValue());
            put(parameterTransformation.getKeyField(), jsonValue, capitalized);
        }
    }
}
