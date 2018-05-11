package io.skalogs.skaetl.service;


import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class TransformatorProcess {

    private final TypeValidation type;

    public Boolean type(TypeValidation typeValidation) {
        return type.equals(typeValidation);
    }

    public abstract void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value);
}
