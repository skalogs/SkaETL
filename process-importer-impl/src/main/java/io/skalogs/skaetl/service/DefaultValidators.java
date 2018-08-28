package io.skalogs.skaetl.service;


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
        genericValidator.register(new BlackListValidator());
        genericValidator.register(new MandatoryFieldValidator());
        genericValidator.register(new MaxFieldValidator());
        genericValidator.register(new MaxMessageSizeValidator());
        genericValidator.register(new FieldExistValidator());
        genericValidator.register(new TimestampValidator());
    }
}
