package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeywordFieldTransformator extends TransformatorProcess {

    public KeywordFieldTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        JsonNode at = at(parameterTransformation.getKeyField(), jsonValue);
        String valueToFormat = at.getNodeType() == JsonNodeType.OBJECT ? at.toString() : at.asText();
        if (StringUtils.isNotBlank(valueToFormat)) {
            put(jsonValue, parameterTransformation.getKeyField() + "_keyword", valueToFormat);
            remove(jsonValue,parameterTransformation.getKeyField());
        }
    }
}
