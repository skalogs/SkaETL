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
        genericValidator.register(TypeValidation.BLACK_LIST_FIELD, new BlackListValidator());
        genericValidator.register(TypeValidation.MANDATORY_FIELD, new MandatoryFieldValidator());
        genericValidator.register(TypeValidation.MAX_FIELD, new MaxFieldValidator());
        genericValidator.register(TypeValidation.MAX_MESSAGE_SIZE, new MaxMessageSizeValidator());
        genericValidator.register(TypeValidation.FIELD_EXIST, new FieldExistValidator());
        genericValidator.register(TypeValidation.TIMESTAMP_VALIDATION, new TimestampValidator());
    }
}
