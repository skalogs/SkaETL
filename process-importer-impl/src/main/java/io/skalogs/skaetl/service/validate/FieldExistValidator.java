package io.skalogs.skaetl.service.validate;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.StatusCode;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.service.ValidatorProcess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldExistValidator extends ValidatorProcess {

    public FieldExistValidator(TypeValidation type) {
        super(type);
    }

    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue, String value) {
        // fields count
        if (has(processValidation.getParameterValidation().getFieldExist(),jsonValue)) {
            return ValidateData.builder()
                    .success(true)
                    .typeValidation(TypeValidation.FIELD_EXIST)
                    .jsonValue(jsonValue)
                    .build();

        } else {
            return ValidateData.builder()
                    .success(false)
                    .statusCode(StatusCode.field_not_exist)
                    .typeValidation(TypeValidation.MAX_FIELD)
                    .message("Field " + processValidation.getParameterValidation().getFieldExist() + " is not present")
                    .jsonValue(jsonValue)
                    .build();
        }

    }


}
