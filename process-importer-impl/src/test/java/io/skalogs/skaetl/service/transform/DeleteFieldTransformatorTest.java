package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DeleteFieldTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        DeleteFieldTransformator deleteFieldValidator = new DeleteFieldTransformator(TypeValidation.DELETE_FIELD);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        deleteFieldValidator.apply(null,
                ParameterTransformation.builder()
                        .keyField("project")
                        .build(),
                jsonValue, value);
        assertThat(jsonValue.path("project").asText()).isEqualTo("");
    }
}
