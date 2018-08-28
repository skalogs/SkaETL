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

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.utils.JSONUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TranslateArrayTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        TranslateArrayTransformator translateArrayTransformator = new TranslateArrayTransformator();

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
        TranslateArrayTransformator translateArrayTransformator = new TranslateArrayTransformator();

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
