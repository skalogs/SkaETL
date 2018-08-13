package io.skalogs.skaetl.service.validate;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.ValidatorProcess;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Slf4j
public class TimestampValidator extends ValidatorProcess {

    private final SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public TimestampValidator(TypeValidation type) {
        super(type);
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
