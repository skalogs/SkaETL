package io.skalogs.skaetl.service.validate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterValidation;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.utils.JSONUtils;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TimestampValidatorTest {
    @Test
    public void should_Process_Ko() throws Exception {
        TimestampValidator timestampValidator = new TimestampValidator(TypeValidation.TIMESTAMP_VALIDATION);
        String timestamp = Instant.now().minus(5, ChronoUnit.DAYS).toString();
        RawDataGen rd = RawDataGen.builder().timestamp(timestamp).build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        JsonNode jsonValue = JSONUtils.getInstance().parse(value);

        ValidateData v = timestampValidator.process(ProcessValidation.builder()
                .parameterValidation(ParameterValidation.builder()
                        .validateInThePast(true)
                        .unitInThePast(1)
                        .chronoUnitInThePast(ChronoUnit.DAYS)
                        .build()
                ).build(), jsonValue);
        assertThat(v.success).isFalse();
    }

    @Test
    public void should_Process_Ok() throws Exception {
        TimestampValidator timestampValidator = new TimestampValidator(TypeValidation.TIMESTAMP_VALIDATION);
        String timestamp = Instant.now().minus(5, ChronoUnit.HOURS).toString();
        RawDataGen rd = RawDataGen.builder().timestamp(timestamp).build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        JsonNode jsonValue = JSONUtils.getInstance().parse(value);

        ValidateData v = timestampValidator.process(ProcessValidation.builder()
                .parameterValidation(ParameterValidation.builder()
                        .validateInThePast(true)
                        .unitInThePast(1)
                        .chronoUnitInThePast(ChronoUnit.DAYS)
                        .build()
                ).build(), jsonValue);
        assertThat(v.success).isTrue();
    }

    @Test
    public void should_Process_Ok_fixed_date() throws Exception {
        TimestampValidator timestampValidator = new TimestampValidator(TypeValidation.TIMESTAMP_VALIDATION);
        String timestamp = Instant.now().minus(5, ChronoUnit.HOURS).toString();
        RawDataGen rd = RawDataGen.builder().timestamp(timestamp).build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        JsonNode jsonValue = JSONUtils.getInstance().parse(value);

        ValidateData v = timestampValidator.process(ProcessValidation.builder()
                .parameterValidation(ParameterValidation.builder()
                        .validateAfterFixedDate(true)
                        .lowerFixedDate("2018-01-01")
                        .build()
                ).build(), jsonValue);
        assertThat(v.success).isTrue();
    }
}