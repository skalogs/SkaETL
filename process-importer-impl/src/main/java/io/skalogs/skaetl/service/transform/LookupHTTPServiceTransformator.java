package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.WorkerHTTPService;
import io.skalogs.skaetl.service.ExternalHTTPService;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Map;

@Slf4j
public class LookupHTTPServiceTransformator extends TransformatorProcess {

    private final ExternalHTTPService externalHTTPService;

    public LookupHTTPServiceTransformator(TypeValidation type, ExternalHTTPService externalHTTPService) {
        super(type);
        this.externalHTTPService = externalHTTPService;
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        WorkerHTTPService workerHTTPService = externalHTTPService.getMapExternalService().get(idProcess);
        if (workerHTTPService != null) {
            String key = parameterTransformation.getKeyField();
            if (StringUtils.isNotBlank(key)) {
                if (jsonValue.has(key)) {
                    String oldValue = jsonValue.path(key).asText();
                    workerHTTPService.getMapResult().entrySet().stream()
                            .filter(entry -> entry.getKey().equals(oldValue))
                            .forEach(entry -> jsonValue.put(key, entry.getValue()));
                }
            } else {
                //All Keys
                workerHTTPService.getMapResult().entrySet().stream()
                        .forEach(entry -> applyMap(jsonValue, entry.getKey(), entry.getValue()));
            }
        }
    }

    private void applyMap(ObjectNode jsonValue, String oldValue, String newValue) {
        for (Iterator<Map.Entry<String, JsonNode>> it = jsonValue.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            if (entry.getValue() != null && entry.getValue().asText().equals(oldValue)) {
                //update
                jsonValue.put(entry.getKey(), newValue);
            }
        }
    }


}