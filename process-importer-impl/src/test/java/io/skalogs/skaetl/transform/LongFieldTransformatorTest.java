package io.skalogs.skaetl.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.transform.LongFieldTransformator;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LongFieldTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        LongFieldTransformator longFieldTransformator = new LongFieldTransformator(TypeValidation.FORMAT_LONG);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        longFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend")
                        .build(),
                jsonValue, value);
        assertThat(jsonValue.path("messageSend_long").asLong()).isEqualTo(new Long(1548));
        assertThat(jsonValue.path("messageSend").asText()).isEqualTo("");
    }

    @Test
    public void should_Process_Ko() throws Exception {
        LongFieldTransformator longFieldTransformator = new LongFieldTransformator(TypeValidation.FORMAT_LONG);
        RawDataGen rd = RawDataGen.builder().messageSend("1548").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        longFieldTransformator.apply(null, ParameterTransformation.builder()
                        .keyField("messageSend2")
                        .build(),
                jsonValue, value);
        assertThat(jsonValue.path("messageSend2_long").asText()).isEqualTo("");
    }


}
