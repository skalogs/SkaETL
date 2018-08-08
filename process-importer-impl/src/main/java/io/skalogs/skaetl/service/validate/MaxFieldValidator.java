package io.skalogs.skaetl.service.validate;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Iterators;
import io.prometheus.client.Histogram;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.StatusCode;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.service.UtilsValidateData;
import io.skalogs.skaetl.service.ValidatorProcess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaxFieldValidator extends ValidatorProcess {

    private static final Histogram eventSizeHistogram = Histogram.build()
            .name("skaetl_event_size")
            .help("event size.")
            .linearBuckets(1, 100, 10)
            .register();

    public MaxFieldValidator(TypeValidation type) {
        super(type);
    }

    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue) {
        // fields count
        int nbFields = Iterators.size(jsonValue.fieldNames());
        eventSizeHistogram.observe(nbFields);
        if (nbFields > processValidation.getParameterValidation().getMaxFields()) {
            return UtilsValidateData.createValidateData(false, StatusCode.max_fields, TypeValidation.MAX_FIELD, jsonValue, String.valueOf(nbFields));
        }
        return ValidateData.builder()
                .success(true)
                .typeValidation(TypeValidation.MAX_FIELD)
                .jsonValue(jsonValue)
                .build();
    }


}
