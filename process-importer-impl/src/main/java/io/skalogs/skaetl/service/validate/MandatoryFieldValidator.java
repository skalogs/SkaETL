package io.skalogs.skaetl.service.validate;

/*-
 * #%L
 * process-importer-impl
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue) {
        if (processValidation.getParameterValidation().getMandatory() != null) {
            String[] tabMandatory = processValidation.getParameterValidation().getMandatory().split(";");
            if (tabMandatory != null && tabMandatory.length > 0) {
                return validateMandatoryField(Arrays.asList(tabMandatory), processValidation, jsonValue);
            } else {
                missingMandatoryFieldsCount.labels("empty").inc();
                return createValidateData(false, StatusCode.missing_mandatory_field, TypeValidation.MANDATORY_FIELD, jsonValue, "Mandatory array is empty");

            }
        } else {
            missingMandatoryFieldsCount.labels("empty").inc();
            return createValidateData(false, StatusCode.missing_mandatory_field, TypeValidation.MANDATORY_FIELD, jsonValue, "Mandatory array is null");
        }
    }

    private ValidateData validateMandatoryField(List<String> tabMandatory, ProcessValidation processValidation, JsonNode jsonValue) {
        List<String> listItemNull = tabMandatory.stream()
                .filter(e -> at(e,jsonValue) == null)
                .collect(toList());
        if (!listItemNull.isEmpty()) {
            listItemNull.forEach(item -> missingMandatoryFieldsCount.labels(item).inc());
            return createValidateData(false, StatusCode.missing_mandatory_field, TypeValidation.MANDATORY_FIELD, jsonValue, listItemNull.stream().collect(Collectors.joining(";")));
        } else {
            return ValidateData.builder()
                    .success(true)
                    .typeValidation(TypeValidation.MANDATORY_FIELD)
                    .jsonValue(jsonValue)
                    .build();
        }
    }


}
