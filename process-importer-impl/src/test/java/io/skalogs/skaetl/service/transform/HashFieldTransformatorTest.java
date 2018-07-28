package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.ProcessHashData;
import io.skalogs.skaetl.domain.TypeHash;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class HashFieldTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        HashFieldTransformator hashFieldTransformator = new HashFieldTransformator(TypeValidation.HASH);

        RawDataGen rd = RawDataGen.builder().messageSend("message gni de test").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        hashFieldTransformator.apply(null, ParameterTransformation.builder()
                .processHashData(ProcessHashData.builder()
                        .field("messageSend")
                        .typeHash(TypeHash.MURMUR3)
                        .build()
                ).build(), jsonValue, value);
        assertThat(jsonValue.path("messageSend").asText()).isEqualTo("7dd5a4ac398698f085e216e25a330f58");
    }

    @Test
    public void should_Process_Ko() throws Exception {
        HashFieldTransformator hashFieldTransformator = new HashFieldTransformator(TypeValidation.HASH);

        RawDataGen rd = RawDataGen.builder().messageSend("").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        hashFieldTransformator.apply(null, ParameterTransformation.builder()
                .processHashData(ProcessHashData.builder()
                        .field("messageSend")
                        .typeHash(TypeHash.MURMUR3)
                        .build()
                ).build(), jsonValue, value);
        assertThat(jsonValue.path("messageSend").asText()).isEqualTo("");
    }

}
