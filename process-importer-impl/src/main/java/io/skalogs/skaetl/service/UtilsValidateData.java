package io.skalogs.skaetl.service;


import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.StatusCode;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class UtilsValidateData {

    public static ValidateData createValidateData(Boolean status, StatusCode statusCode, TypeValidation typeValidation, JsonNode value) {
        return ValidateData.builder()
                .success(status)
                .errorList(Arrays.asList(statusCode))
                .typeValidation(typeValidation)
                .statusCode(statusCode)
                .value(value.toString())
                .build();
    }

    public static ValidateData createValidateData(Boolean status, StatusCode statusCode, TypeValidation typeValidation, JsonNode value, String message) {
        return ValidateData.builder()
                .success(status)
                .errorList(Arrays.asList(statusCode))
                .typeValidation(typeValidation)
                .statusCode(statusCode)
                .value(value.toString())
                .message(message)
                .build();
    }

    public static ValidateData createValidateData(Boolean status, StatusCode statusCode, List<StatusCode> listErrors, TypeValidation typeValidation, JsonNode value) {
        return ValidateData.builder()
                .success(status)
                .errorList(listErrors)
                .typeValidation(typeValidation)
                .statusCode(statusCode)
                .value(value.toString())
                .build();
    }

    public static ValidateData createValidateData(Boolean status, StatusCode statusCode, List<StatusCode> listErrors, TypeValidation typeValidation, String value,String message) {
        return ValidateData.builder()
                .success(status)
                .errorList(listErrors)
                .typeValidation(typeValidation)
                .statusCode(statusCode)
                .value(value)
                .message(message)
                .build();
    }

    public static ValidateData createValidateData(String project, String type, Boolean status, StatusCode statusCode, TypeValidation typeValidation, String value, String message) {
        return ValidateData.builder()
                .success(status)
                .errorList(Arrays.asList(statusCode))
                .typeValidation(typeValidation)
                .statusCode(statusCode)
                .value(value)
                .project(project)
                .type(type)
                .message(message)
                .build();
    }

    public static ValidateData createValidateData(String project, String type, Boolean status, StatusCode statusCode, TypeValidation typeValidation, JsonNode value) {
        return ValidateData.builder()
                .success(status)
                .errorList(Arrays.asList(statusCode))
                .typeValidation(typeValidation)
                .statusCode(statusCode)
                .value(value.toString())
                .project(project)
                .type(type)
                .build();
    }

    public static ValidateData createValidateData(JsonNode jsonObject, String project, String type, Boolean status, StatusCode statusCode, TypeValidation typeValidation) {
        return ValidateData.builder()
                .success(status)
                .errorList(Arrays.asList(statusCode))
                .typeValidation(typeValidation)
                .statusCode(statusCode)
                .value(jsonObject.toString())
                .project(project)
                .type(type)
                .jsonValue(jsonObject)
                .build();
    }

    public static ValidateData createValidateData(JsonNode jsonObject, Date timestamp, String project, String type, Boolean status) {
        return ValidateData.builder()
                .success(status)
                .timestamp(timestamp)
                .value(jsonObject.toString())
                .project(project)
                .type(type)
                .jsonValue(jsonObject)
                .build();
    }
}
