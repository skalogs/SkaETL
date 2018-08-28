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

    public MaxFieldValidator() {
        super(TypeValidation.MAX_FIELD,"Max field validator");
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
