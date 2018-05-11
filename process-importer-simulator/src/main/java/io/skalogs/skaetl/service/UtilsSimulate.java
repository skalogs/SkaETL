package io.skalogs.skaetl.service;


import io.skalogs.skaetl.domain.SimulateData;
import io.skalogs.skaetl.domain.ValidateData;
import org.apache.commons.lang.StringUtils;

import java.util.stream.Collectors;

public final class UtilsSimulate {

    public static SimulateData generateFromValidateData(String textInput, ValidateData validateData) {
        StringBuilder sb = new StringBuilder();
        if (validateData.errorList != null && !validateData.errorList.isEmpty()) {
            sb.append(" cause : " + validateData.errorList.stream().map(e -> e.name()).collect(Collectors.joining(" and ")));
        } else if (validateData.statusCode != null) {
            sb.append(" cause : " + validateData.statusCode);
        }
        if (validateData.message != null && StringUtils.isNotBlank(validateData.message)) {
            sb.append(validateData.message);
        }
        if (!"OK".equals(validateData.message)) {
            sb.append(" with value : " + validateData.value);
        }
        return SimulateData.builder()
                .jsonValue(validateData.jsonValue)
                .message(sb.toString())
                .value(textInput)
                .build();
    }

}
