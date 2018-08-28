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
import io.prometheus.client.Histogram;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.StatusCode;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.service.ValidatorProcess;
import lombok.extern.slf4j.Slf4j;

import static io.skalogs.skaetl.service.UtilsValidateData.createValidateData;

@Slf4j
public class MaxMessageSizeValidator extends ValidatorProcess {

    private static final Histogram nbFieldsHistogram = Histogram.build()
            .name("skaetl_nb_fields")
            .help("nb fields.")
            .linearBuckets(1, 10, 10)
            .register();

    public MaxMessageSizeValidator() {
        super(TypeValidation.MAX_MESSAGE_SIZE,"Max message size validator");
    }


    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue) {
        int eventSize = jsonValue.toString().length();
        nbFieldsHistogram.observe(eventSize);
        if (eventSize > processValidation.getParameterValidation().getMaxMessageSize()) {
            return createValidateData(false, StatusCode.event_size, TypeValidation.MAX_MESSAGE_SIZE, jsonValue, String.valueOf(eventSize));
        }
        return ValidateData.builder()
                .success(true)
                .typeValidation(TypeValidation.MAX_MESSAGE_SIZE)
                .jsonValue(jsonValue)
                .build();
    }


}
