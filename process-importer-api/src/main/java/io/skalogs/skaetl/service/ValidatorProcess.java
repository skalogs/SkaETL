package io.skalogs.skaetl.service;


import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class ValidatorProcess {

    private final TypeValidation type;

    private final JSONUtils jsonUtils = JSONUtils.getInstance();

    public abstract ValidateData process(ProcessValidation processValidation, JsonNode jsonValue);

    public Boolean type(TypeValidation typeValidation) {
        return type.equals(typeValidation);
    }

    protected boolean has(String path, JsonNode jsonNode) {
        return jsonUtils.has(jsonNode, path);
    }

    protected JsonNode at(String path, JsonNode jsonNode) {
        return jsonUtils.at(jsonNode, path);
    }
}
