package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LowerCaseTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        LowerCaseTransformator lowerCaseTransformator = new LowerCaseTransformator(TypeValidation.LOWER_CASE);
        RawDataGen rd = RawDataGen.builder().messageSend("GNI GNU").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        lowerCaseTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("messageSend")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("messageSend").asText()).isEqualTo("gni gnu");
    }
}