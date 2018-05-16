package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.prometheus.client.Counter;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.validate.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.skalogs.skaetl.service.UtilsValidateData.createValidateData;
import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class GenericValidator {

    private static final Counter nbMandatoryImporter = Counter.build()
            .name("skaetl_nb_mandatory_importer")
            .labelNames("type")
            .help("nb message not json.")
            .register();

    private final ISO8601DateFormat dateFormat = new ISO8601DateFormat();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<ValidatorProcess> listValidator = new ArrayList<>();

    @PostConstruct
    public void init() {
        listValidator.add(new BlackListValidator(TypeValidation.BLACK_LIST_FIELD));
        listValidator.add(new MandatoryFieldValidator(TypeValidation.MANDATORY_FIELD));
        listValidator.add(new MaxFieldValidator(TypeValidation.MAX_FIELD));
        listValidator.add(new MaxMessageSizeValidator(TypeValidation.MAX_MESSAGE_SIZE));
        listValidator.add(new FieldExistValidator(TypeValidation.FIELD_EXIST));
    }

    public JsonNode createJsonObject(String value) {
        try {
            return objectMapper.readTree(value);
        } catch (IOException e) {
            nbMandatoryImporter.labels("jsonFormat").inc();
            return null;
        }
    }


    public ValidateData mandatoryImporter(String value, JsonNode jsonValue) {
        //JSON
        if (jsonValue == null) {
            nbMandatoryImporter.labels("jsonFormat").inc();
            return createValidateData(false, StatusCode.invalid_json, TypeValidation.FORMAT_JSON, value);
        }
        //PROJECT
        String project = jsonValue.path("project").asText();
        if (StringUtils.isBlank(project)) {
            nbMandatoryImporter.labels("project").inc();
            return createValidateData(false, StatusCode.missing_mandatory_field_project, TypeValidation.MANDATORY_FIELD, value, "missing project");
        }
        //TYPE
        String type = jsonValue.path("type").asText();
        if (StringUtils.isBlank(type)) {
            nbMandatoryImporter.labels("type").inc();
            return createValidateData(false, StatusCode.missing_mandatory_field_type, TypeValidation.MANDATORY_FIELD, value, "missing type");
        }
        //TIMESTAMP
        String timestampAnnotedAsString = jsonValue.path("@timestamp").asText();
        String timestampAsString = jsonValue.path("timestamp").asText();

        if (StringUtils.isBlank(timestampAsString) && StringUtils.isBlank(timestampAnnotedAsString)) {
            return createValidateData(project, type, false, StatusCode.missing_timestamp, TypeValidation.MANDATORY_FIELD, value);
        }

        Date timestamp;
        try {
            if (StringUtils.isBlank(timestampAsString)) {
                timestamp = dateFormat.parse(timestampAnnotedAsString);
            } else {
                timestamp = dateFormat.parse(timestampAsString);
            }

        } catch (ParseException e) {
            return createValidateData(jsonValue, project, type, false, StatusCode.invalid_format_timestamp, TypeValidation.MANDATORY_FIELD, value);
        }
        return createValidateData(jsonValue, timestamp, project, type, true, value);
    }

    public ValidateData process(String value, ProcessConsumer processConsumer) {
        JsonNode jsonValue = createJsonObject(value);

        ValidateData validateMandatory = mandatoryImporter(value, jsonValue);
        if (!validateMandatory.success) {
            return createValidateData(false, validateMandatory.statusCode, validateMandatory.errorList, TypeValidation.MANDATORY_FIELD, value);
        }
        List<ValidateData> result = treat(value, jsonValue, processConsumer);
        List<ValidateData> listNotSuccess = result.stream().filter(e -> !e.success).collect(toList());
        if (!listNotSuccess.isEmpty()) {
            return createValidateData(false, validateMandatory.statusCode, listNotSuccess.stream().map(e -> e.getStatusCode()).collect(toList()), TypeValidation.MANDATORY_FIELD, value);
        }
        return ValidateData.builder()
                .success(true)
                .jsonValue(jsonValue)
                .type(validateMandatory.type)
                .project(validateMandatory.project)
                .timestamp(validateMandatory.timestamp)
                .value(value).build();

    }

    public List<ValidateData> treat(String value, JsonNode jsonValue, ProcessConsumer processConsumer) {
        List<ValidateData> result = new ArrayList<>();
        if (processConsumer.getProcessValidation() != null && !processConsumer.getProcessValidation().isEmpty()) {
            for (ProcessValidation pv : processConsumer.getProcessValidation()) {
                listValidator.stream()
                        .filter(e -> e.type(pv.getTypeValidation()))
                        .forEach(e -> result.add(e.process(pv, jsonValue, value)));
            }
        }
        return result;
    }


}

