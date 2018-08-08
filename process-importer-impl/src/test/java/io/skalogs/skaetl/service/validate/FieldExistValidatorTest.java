package io.skalogs.skaetl.service.validate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterValidation;
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class FieldExistValidatorTest {

    @Test
    public void should_Process_Ok() throws Exception {
        FieldExistValidator fieldExistValidator = new FieldExistValidator(TypeValidation.FIELD_EXIST);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        JsonNode jsonValue = JSONUtils.getInstance().parse(value);

        ValidateData v = fieldExistValidator.process(ProcessValidation.builder()
                .parameterValidation(ParameterValidation.builder()
                        .fieldExist("messageSend")
                        .build()
                ).build(), jsonValue);
        assertThat(v.success).isTrue();
    }

    @Test
    public void should_Process_Ko() throws Exception {
        FieldExistValidator fieldExistValidator = new FieldExistValidator(TypeValidation.FIELD_EXIST);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        JsonNode jsonValue = JSONUtils.getInstance().parse(value);

        ValidateData v = fieldExistValidator.process(ProcessValidation.builder()
                .parameterValidation(ParameterValidation.builder()
                        .fieldExist("toto")
                        .build()
                ).build(), jsonValue);
        assertThat(v.success).isFalse();
    }

}
