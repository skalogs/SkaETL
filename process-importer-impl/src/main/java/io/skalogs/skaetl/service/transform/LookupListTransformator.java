package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class LookupListTransformator extends TransformatorProcess {

    public LookupListTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        String key = parameterTransformation.getKeyField();
        if (StringUtils.isNotBlank(key)) {
            if (jsonValue.has(key)) {
                String oldValue = jsonValue.path(key).asText();
                parameterTransformation.getMapLookup().entrySet().stream()
                        .filter(entry -> entry.getKey().equals(oldValue))
                        .forEach(entry -> jsonValue.put(key, entry.getValue()));
            }
        }
    }

}