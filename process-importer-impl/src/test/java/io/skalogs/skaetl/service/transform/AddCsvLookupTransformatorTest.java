package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.CsvLookupData;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.ProcessKeyValue;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AddCsvLookupTransformatorTest {

    @Test
    public void should_Process_Ok() throws Exception {
        AddCsvLookupTransformator addCsvLookupTransformator = new AddCsvLookupTransformator(TypeValidation.ADD_CSV_LOOKUP);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project value").type("typeToDefine").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        HashMap<String, List<ProcessKeyValue>> mapTest = new HashMap<>();
        mapTest.put("typeToDefine", Arrays.asList(ProcessKeyValue.builder().key("ren.mouarf").value("toto").build(), ProcessKeyValue.builder().key("multipla").value("fiat").build()));
        mapTest.put("fake", Arrays.asList(ProcessKeyValue.builder().key("fakeKey").value("fakeValue").build(), ProcessKeyValue.builder().build()));

        addCsvLookupTransformator.apply(null,
                ParameterTransformation.builder()
                        .csvLookupData(CsvLookupData.builder()
                                .field("type")
                                .map(mapTest)
                                .build())
                        .build(),
                jsonValue);
        assertThat(jsonValue.get("type").asText()).isEqualTo("typeToDefine");
        assertThat(jsonValue.get("ren").get("mouarf").asText()).isEqualTo("toto");
        assertThat(jsonValue.get("multipla").asText()).isEqualTo("fiat");
    }

    @Test
    public void should_Process_Ko() throws Exception {
        AddCsvLookupTransformator addCsvLookupTransformator = new AddCsvLookupTransformator(TypeValidation.ADD_CSV_LOOKUP);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project value").type("typeToDefine").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        HashMap<String, List<ProcessKeyValue>> mapTest = new HashMap<>();
        mapTest.put("typeToDefine", Arrays.asList(ProcessKeyValue.builder().key("ren.mouarf").value("toto").build(), ProcessKeyValue.builder().key("multipla").value("fiat").build()));
        mapTest.put("fake", Arrays.asList(ProcessKeyValue.builder().key("fakeKey").value("fakeValue").build(), ProcessKeyValue.builder().build()));

        addCsvLookupTransformator.apply(null,
                ParameterTransformation.builder()
                        .csvLookupData(CsvLookupData.builder()
                                .field("project")
                                .map(mapTest)
                                .build())
                        .build(),
                jsonValue);
        assertThat(jsonValue.get("type").asText()).isEqualTo("typeToDefine");
        assertThat(jsonValue.has("multipla")).isFalse();
    }

}
