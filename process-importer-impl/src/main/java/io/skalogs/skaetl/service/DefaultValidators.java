package io.skalogs.skaetl.service;


import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.validate.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Lazy(value = false)
@AllArgsConstructor
public class DefaultValidators {


    private final GenericValidator genericValidator;

    @PostConstruct
    public void initDefaults() {
        genericValidator.register(TypeValidation.BLACK_LIST_FIELD, new BlackListValidator(TypeValidation.BLACK_LIST_FIELD));
        genericValidator.register(TypeValidation.MANDATORY_FIELD, new MandatoryFieldValidator(TypeValidation.MANDATORY_FIELD));
        genericValidator.register(TypeValidation.MAX_FIELD, new MaxFieldValidator(TypeValidation.MAX_FIELD));
        genericValidator.register(TypeValidation.MAX_MESSAGE_SIZE, new MaxMessageSizeValidator(TypeValidation.MAX_MESSAGE_SIZE));
        genericValidator.register(TypeValidation.FIELD_EXIST, new FieldExistValidator(TypeValidation.FIELD_EXIST));
        genericValidator.register(TypeValidation.TIMESTAMP_VALIDATION, new TimestampValidator(TypeValidation.TIMESTAMP_VALIDATION));
    }
}
