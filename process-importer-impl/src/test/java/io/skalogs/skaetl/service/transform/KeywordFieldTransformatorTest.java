package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KeywordFieldTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        KeywordFieldTransformator keywordFieldTransformator = new KeywordFieldTransformator(TypeValidation.FORMAT_KEYWORD);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        keywordFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("messageSend_keyword").asText()).isEqualTo("1548");
        assertThat(jsonValue.path("messageSend").asText()).isEqualTo("");
    }

    @Test
    public void should_Process_nestedObject_Ok() throws Exception {
        KeywordFieldTransformator keywordFieldTransformator = new KeywordFieldTransformator(TypeValidation.FORMAT_KEYWORD);
        String value = "{\"something\":\"test\",\"comment\": {\"value\":\"value1\"}}";
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        keywordFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("comment")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("comment_keyword").asText()).isEqualTo("{\"value\":\"value1\"}");
        assertThat(jsonValue.path("comment").asText()).isEqualTo("");
    }


    @Test
    public void should_Process_Ko() throws Exception {
        KeywordFieldTransformator keywordFieldTransformator = new KeywordFieldTransformator(TypeValidation.FORMAT_KEYWORD);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        keywordFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend2")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("messageSend2_keyword").asText()).isEqualTo("");
    }
}