package io.skalogs.skaetl.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.ProcessKeyValue;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.transform.AddFieldTransformator;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AddFieldTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        AddFieldTransformator addFieldValidator = new AddFieldTransformator(TypeValidation.ADD_FIELD);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        addFieldValidator.apply(null, ParameterTransformation.builder()
                .composeField(ProcessKeyValue.builder()
                        .key("addField")
                        .value("value")
                        .build()
                ).build(), jsonValue, value);
        assertThat(jsonValue.path("addField").asText()).isEqualTo("value");
    }

}
