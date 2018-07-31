package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class AddCsvLookupTransformator extends TransformatorProcess {

    public AddCsvLookupTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        String field = parameterTransformation.getCsvLookupData().getField();
        if (StringUtils.isNotBlank(field)) {
            if (has(field, jsonValue)) {
                String valueField = jsonValue.path(field).asText();
                if (parameterTransformation.getCsvLookupData().getMap().get(valueField) != null &&
                        !parameterTransformation.getCsvLookupData().getMap().get(valueField).isEmpty()) {
                    parameterTransformation.getCsvLookupData().getMap().get(valueField)
                            .stream()
                            .forEach(processKeyValue -> put(jsonValue, processKeyValue.getKey(), processKeyValue.getValue()));
                }
            }
        }
    }

}