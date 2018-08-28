package io.skalogs.skaetl.service;

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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.skalogs.skaetl.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class GenericValidator {

    private final ISO8601DateFormat dateFormat = new ISO8601DateFormat();
    private Map<TypeValidation, ValidatorProcess> validators = new HashMap<>();


    public void register(ValidatorProcess validatorProcess) {
        register(validatorProcess.getType(),validatorProcess);
    }

    public void register(TypeValidation typeValidation, ValidatorProcess validatorProcess) {
        validators.put(typeValidation, validatorProcess);
    }

    public ValidateData mandatoryImporter(ObjectNode jsonValue) {
        //JSON
        if (jsonValue == null) {
            Metrics.counter("skaetl_nb_mandatory_importer", Lists.newArrayList(Tag.of("type", "jsonFormat"))).increment();
            return UtilsValidateData.createValidateData(false, StatusCode.invalid_json, TypeValidation.FORMAT_JSON, jsonValue);
        }
        //PROJECT
        String project = jsonValue.path("project").asText();
        if (StringUtils.isBlank(project)) {
            Metrics.counter("skaetl_nb_mandatory_importer", Lists.newArrayList(Tag.of("type", "project"))).increment();
            return UtilsValidateData.createValidateData(false, StatusCode.missing_mandatory_field_project, TypeValidation.MANDATORY_FIELD, jsonValue, "missing project");
        }
        //TYPE
        String type = jsonValue.path("type").asText();
        if (StringUtils.isBlank(type)) {
            Metrics.counter("skaetl_nb_mandatory_importer", Lists.newArrayList(Tag.of("type", "type"))).increment();
            return UtilsValidateData.createValidateData(false, StatusCode.missing_mandatory_field_type, TypeValidation.MANDATORY_FIELD, jsonValue, "missing type");
        }
        //TIMESTAMP
        String timestampAnnotedAsString = jsonValue.path("@timestamp").asText();
        String timestampAsString = jsonValue.path("timestamp").asText();

        if (StringUtils.isBlank(timestampAsString) && StringUtils.isBlank(timestampAnnotedAsString)) {
            return UtilsValidateData.createValidateData(project, type, false, StatusCode.missing_timestamp, TypeValidation.MANDATORY_FIELD, jsonValue);
        }

        Date timestamp;
        try {
            if (StringUtils.isBlank(timestampAsString)) {
                timestamp = dateFormat.parse(timestampAnnotedAsString);
                jsonValue.set("timestamp", jsonValue.path("@timestamp"));
            } else {
                timestamp = dateFormat.parse(timestampAsString);
            }
        } catch (ParseException e) {
            return UtilsValidateData.createValidateData(jsonValue, project, type, false, StatusCode.invalid_format_timestamp, TypeValidation.MANDATORY_FIELD);
        }
        return UtilsValidateData.createValidateData(jsonValue, timestamp, project, type, true);
    }

    public ValidateData process(ObjectNode value, ProcessConsumer processConsumer) {

        ValidateData validateMandatory = mandatoryImporter(value);
        if (!validateMandatory.success) {
            return UtilsValidateData.createValidateData(false, validateMandatory.statusCode, validateMandatory.errorList, TypeValidation.MANDATORY_FIELD, value);
        }
        List<ValidateData> result = treat(value, processConsumer);
        List<ValidateData> listNotSuccess = result.stream().filter(e -> !e.success).collect(toList());
        if (!listNotSuccess.isEmpty()) {
            return UtilsValidateData.createValidateData(false, validateMandatory.statusCode, listNotSuccess.stream().map(e -> e.getStatusCode()).collect(toList()), TypeValidation.MANDATORY_FIELD, value);
        }
        return ValidateData.builder()
                .success(true)
                .jsonValue(value)
                .type(validateMandatory.type)
                .project(validateMandatory.project)
                .timestamp(validateMandatory.timestamp)
                .value(value.toString()).build();

    }

    public List<ValidateData> treat(JsonNode jsonValue, ProcessConsumer processConsumer) {
        List<ValidateData> result = new ArrayList<>();
        if (processConsumer.getProcessValidation() != null && !processConsumer.getProcessValidation().isEmpty()) {
            for (ProcessValidation pv : processConsumer.getProcessValidation()) {
                if (validators.containsKey(pv.getTypeValidation())) {
                    result.add(validators.get(pv.getTypeValidation()).process(pv, jsonValue));
                }
            }
        }
        return result;
    }

    public List<ValidatorDescription> validatorDescriptions() {
        return validators
                .values()
                .stream()
                .map((e) -> new ValidatorDescription(e.getType().name(),e.getDescription()))
                .collect(Collectors.toList());
    }



}

