package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TranslateArrayTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        TranslateArrayTransformator translateArrayTransformator = new TranslateArrayTransformator(TypeValidation.TRANSLATE_ARRAY);

        String value = "{" +
                "    \"tags\" : [\"One\", \"Two\", \"Three\"]" +
                "}";
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        translateArrayTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("tags")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("tags-one").asText()).isEqualTo("true");
        assertThat(jsonValue.path("tags-two").asText()).isEqualTo("true");
        assertThat(jsonValue.path("tags-three").asText()).isEqualTo("true");
    }

    @Test
    public void should_Process_Ko() throws Exception {
        TranslateArrayTransformator translateArrayTransformator = new TranslateArrayTransformator(TypeValidation.TRANSLATE_ARRAY);

        String value = "{" +
                "    \"tags\" : [\"One\", \"Two\", \"Three\"]" +
                "}";
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        translateArrayTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("missing")
                        .build(),
                jsonValue);
        assertThat(jsonValue.path("tags-one").isMissingNode()).isTrue();
        assertThat(jsonValue.path("tags-two").isMissingNode()).isTrue();
        assertThat(jsonValue.path("tags-three").isMissingNode()).isTrue();
    }
}