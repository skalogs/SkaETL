package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class RenameFieldTransformator extends TransformatorProcess {

    public RenameFieldTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        if (jsonValue.get(parameterTransformation.getComposeField().getKey()) != null) {
            JsonNode valueField = jsonValue.path(parameterTransformation.getComposeField().getKey());
            if (StringUtils.isNotBlank(valueField.asText())) {
                jsonValue.set(parameterTransformation.getComposeField().getValue(), valueField);
            } else {
                jsonValue.put(parameterTransformation.getComposeField().getValue(), "");
            }
            jsonValue.remove(parameterTransformation.getComposeField().getKey());
        }
    }
}