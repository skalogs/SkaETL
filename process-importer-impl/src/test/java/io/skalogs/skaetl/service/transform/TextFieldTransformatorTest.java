package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextFieldTransformatorTest {

    @Test
    public void should_Process_Ok() throws Exception {
        TextFieldTransformator textFieldTransformator = new TextFieldTransformator(TypeValidation.FORMAT_TEXT);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        textFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("messageSend_text").asText()).isEqualTo("1548");
        assertThat(jsonValue.path("messageSend").asText()).isEqualTo("");
    }

    @Test
    public void should_Process_nestedObject_Ok() throws Exception {
        TextFieldTransformator textFieldTransformator = new TextFieldTransformator(TypeValidation.FORMAT_TEXT);
        String value = "{\"something\":\"test\",\"comment\": {\"value\":\"value1\"}}";
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        textFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("comment")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("comment_text").asText()).isEqualTo("{\"value\":\"value1\"}");
        assertThat(jsonValue.path("comment").asText()).isEqualTo("");
    }


    @Test
    public void should_Process_Ko() throws Exception {
        TextFieldTransformator textFieldTransformator = new TextFieldTransformator(TypeValidation.FORMAT_TEXT);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        textFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend2")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("messageSend2_text").asText()).isEqualTo("");
    }

}