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
public class BooleanTransformatorTest {

    @Test
    public void should_Process_Ok() throws Exception {
        BooleanTransformator booleanTransformator = new BooleanTransformator(TypeValidation.FORMAT_BOOLEAN);
        RawDataGen rd = RawDataGen.builder().messageSend("true").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        booleanTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("messageSend")
                        .build(),
                jsonValue, value);

        assertThat(jsonValue.path("messageSend").asBoolean()).isEqualTo(true);
    }


    @Test
    public void should_Process_Ko() throws Exception {
        BooleanTransformator booleanTransformator = new BooleanTransformator(TypeValidation.FORMAT_BOOLEAN);
        RawDataGen rd = RawDataGen.builder().messageSend("true").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        booleanTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("toto")
                        .build(),
                jsonValue, value);

        assertThat(jsonValue.path("toto").asText()).isEqualTo("");
    }

    @Test
    public void should_Process_Upper_Ok() throws Exception {
        BooleanTransformator booleanTransformator = new BooleanTransformator(TypeValidation.FORMAT_BOOLEAN);
        RawDataGen rd = RawDataGen.builder().messageSend("TRUE").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        booleanTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("messageSend")
                        .build(),
                jsonValue, value);

        assertThat(jsonValue.path("messageSend").asBoolean()).isEqualTo(true);
    }


}
