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
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.ValidatorProcess;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
public class TimestampValidator extends ValidatorProcess {

    private final SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public TimestampValidator() {
        super(TypeValidation.TIMESTAMP_VALIDATION,"Timestamp validator");
    }

    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue) {
        JsonNode timestampNode = at("timestamp", jsonValue);
        if (timestampNode.isTextual()) {
            try {
                Date timestamp = isoDateFormat.parse(timestampNode.asText());
                ParameterValidation parameterValidation = processValidation.getParameterValidation();


                if (parameterValidation.isValidateInThePast()) {
                    Instant lowestDate = Instant.now().minus(parameterValidation.getUnitInThePast(), parameterValidation.getChronoUnitInThePast());
                    if (timestamp.toInstant().isBefore(lowestDate)) {
                        return ValidateData.builder()
                                .success(false)
                                .statusCode(StatusCode.timestamp_too_much_in_past)
                                .typeValidation(TypeValidation.TIMESTAMP_VALIDATION)
                                .jsonValue(jsonValue)
                                .build();
                    }
                }

                if (parameterValidation.isValidateAfterFixedDate()) {

                    Instant lowestDate = LocalDate.parse(parameterValidation.getLowerFixedDate()).atStartOfDay().toInstant(ZoneOffset.UTC);
                    if (timestamp.toInstant().isBefore(lowestDate)) {
                        return ValidateData.builder()
                                .success(false)
                                .statusCode(StatusCode.timestamp_too_much_in_past)
                                .typeValidation(TypeValidation.TIMESTAMP_VALIDATION)
                                .jsonValue(jsonValue)
                                .build();
                    }

                }


                if (parameterValidation.isValidateInFuture()) {
                    Instant highestDate = Instant.now().plus(parameterValidation.getUnitInFuture(), parameterValidation.getChronoUnitInFuture());
                    if (timestamp.toInstant().isAfter(highestDate)) {
                        return ValidateData.builder()
                                .success(false)
                                .statusCode(StatusCode.timestamp_too_much_in_future)
                                .typeValidation(TypeValidation.TIMESTAMP_VALIDATION)
                                .jsonValue(jsonValue)
                                .build();
                    }
                }

                return ValidateData.builder()
                        .success(true)
                        .typeValidation(TypeValidation.TIMESTAMP_VALIDATION)
                        .jsonValue(jsonValue)
                        .build();
            } catch (ParseException e) {
                log.error("Can't parse timestamp", e);
            }
        }
        return ValidateData.builder()
                .success(false)
                .statusCode(StatusCode.invalid_format_timestamp)
                .typeValidation(TypeValidation.TIMESTAMP_VALIDATION)
                .jsonValue(jsonValue)
                .build();
    }


}
