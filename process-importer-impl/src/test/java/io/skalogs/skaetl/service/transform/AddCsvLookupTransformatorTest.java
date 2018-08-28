package io.skalogs.skaetl.service.transform;

/*-
 * #%L
 * process-importer-impl
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.CsvLookupData;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.ProcessKeyValue;
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
        AddCsvLookupTransformator addCsvLookupTransformator = new AddCsvLookupTransformator();
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
        AddCsvLookupTransformator addCsvLookupTransformator = new AddCsvLookupTransformator();
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
