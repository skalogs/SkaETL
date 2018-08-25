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
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LookupListTransformatorTest {

    @Test
    public void should_Process_Ok() throws Exception {
        LookupListTransformator lookupListTransformator = new LookupListTransformator(TypeValidation.LOOKUP_LIST);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project value").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);
        HashMap<String, String> mapTest = new HashMap<>();
        mapTest.put("gni", "new value of message");
        mapTest.put("project value", "new value of project");

        lookupListTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("project")
                        .mapLookup(mapTest)
                        .build(),
                jsonValue);
        assertThat(jsonValue.get("messageSend").asText()).isEqualTo("gni");
        assertThat(jsonValue.get("project").asText()).isEqualTo("new value of project");
        assertThat(jsonValue.get("type").asText()).isEqualTo("type");
    }

    @Test
    public void should_Process_Limit_Ok() throws Exception {
        LookupListTransformator lookupListTransformator = new LookupListTransformator(TypeValidation.LOOKUP_LIST);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project value").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);
        HashMap<String, String> mapTest = new HashMap<>();
        mapTest.put("gni", "new value of message");
        mapTest.put("project value", "new value of project");

        lookupListTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("messageSend")
                        .mapLookup(mapTest)
                        .build(),
                jsonValue);
        assertThat(jsonValue.get("messageSend").asText()).isEqualTo("new value of message");
        assertThat(jsonValue.get("project").asText()).isEqualTo("project value");
        assertThat(jsonValue.get("type").asText()).isEqualTo("type");
    }

    @Test
    public void should_Process_Limit_Ko() throws Exception {
        LookupListTransformator lookupListTransformator = new LookupListTransformator(TypeValidation.LOOKUP_LIST);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project value").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);
        HashMap<String, String> mapTest = new HashMap<>();
        mapTest.put("gni", "new value of message");
        mapTest.put("project value", "new value of project");

        lookupListTransformator.apply(null,
                ParameterTransformation.builder()
                        .keyField("gni")
                        .mapLookup(mapTest)
                        .build(),
                jsonValue);
        assertThat(jsonValue.get("messageSend").asText()).isEqualTo("gni");
        assertThat(jsonValue.get("project").asText()).isEqualTo("project value");
        assertThat(jsonValue.get("type").asText()).isEqualTo("type");
    }


}
