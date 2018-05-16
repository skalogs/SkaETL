package io.skalogs.skaetl.service.validate;

import com.fasterxml.jackson.databind.JsonNode;
import io.prometheus.client.Counter;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.StatusCode;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.service.ValidatorProcess;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.skalogs.skaetl.service.UtilsValidateData.createValidateData;
import static java.util.stream.Collectors.toList;


@Slf4j
public class MandatoryFieldValidator extends ValidatorProcess {
    private static final String UNKNOWN = "unknown";

    private static final Counter missingMandatoryFieldsCount = Counter.build()
            .name("skaetl_nb_missing_mandatory_field_count")
            .labelNames("fieldname")
            .help("nb missing mandatory field count.")
            .register();

    public MandatoryFieldValidator(TypeValidation type) {
        super(type);
    }

    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue, String value) {
        if (processValidation.getParameterValidation().getMandatory() != null) {
            String[] tabMandatory = processValidation.getParameterValidation().getMandatory().split(";");
            if (tabMandatory != null && tabMandatory.length > 0) {
                return validateMandatoryField(Arrays.asList(tabMandatory), processValidation, jsonValue, value);
            } else {
                missingMandatoryFieldsCount.labels("empty").inc();
                return createValidateData(false, StatusCode.missing_mandatory_field, TypeValidation.MANDATORY_FIELD, value, "Mandatory array is empty");

            }
        } else {
            missingMandatoryFieldsCount.labels("empty").inc();
            return createValidateData(false, StatusCode.missing_mandatory_field, TypeValidation.MANDATORY_FIELD, value, "Mandatory array is null");
        }
    }

    private ValidateData validateMandatoryField(List<String> tabMandatory, ProcessValidation processValidation, JsonNode jsonValue, String value) {
        List<String> listItemNull = tabMandatory.stream()
                .filter(e -> jsonValue.get(e) == null)
                .collect(toList());
        if (!listItemNull.isEmpty()) {
            listItemNull.forEach(item -> missingMandatoryFieldsCount.labels(item).inc());
            return createValidateData(false, StatusCode.missing_mandatory_field, TypeValidation.MANDATORY_FIELD, value, listItemNull.stream().collect(Collectors.joining(";")));
        } else {
            return ValidateData.builder()
                    .success(true)
                    .typeValidation(TypeValidation.MANDATORY_FIELD)
                    .jsonValue(jsonValue)
                    .build();
        }
    }


}
