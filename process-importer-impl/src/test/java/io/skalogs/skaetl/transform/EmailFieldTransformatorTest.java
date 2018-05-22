package io.skalogs.skaetl.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.transform.EmailFormatTransformator;
import io.skalogs.skaetl.service.transform.LongFieldTransformator;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EmailFieldTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        EmailFormatTransformator longFieldTransformator = new EmailFormatTransformator(TypeValidation.FORMAT_EMAIL);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        longFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend")
                        .build(),
                jsonValue, value);
        assertThat(jsonValue.path("messageSend_ue").asLong()).isEqualTo(new Long(1548));
        assertThat(jsonValue.path("messageSend").asText()).isEqualTo("");
    }

    @Test
    public void should_Process_Ko() throws Exception {
        EmailFormatTransformator longFieldTransformator = new EmailFormatTransformator(TypeValidation.FORMAT_EMAIL);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        longFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend2")
                        .build(),
                jsonValue, value);
        assertThat(jsonValue.path("messageSend2_ue").asText()).isEqualTo("");
        assertThat(jsonValue.has("messageSend2_ue")).isFalse();
    }


}
