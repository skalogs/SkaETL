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
