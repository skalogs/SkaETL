package io.skalogs.skaetl.parser;

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

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.service.parser.CSVParser;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CSVParserTest {
    @Test
    public void should_error() {
        CSVParser csvParser = new CSVParser();
        String value = "aaaa;bbbbb;cccccc;ddddddddd;zzzzzzz";
        String result = csvParser.process(value, ProcessParser.builder().schemaCSV("key1;key2;key3;key4").build()).getMessageFailParse();
        assertThat(result).contains("CSVParser Exception");
    }

    @Test
    public void should_work() {
        CSVParser csvParser = new CSVParser();
        String value = "aaaa;bbbbb;cccccc;ddddddddd;";
        String result = csvParser.process(value, ProcessParser.builder().schemaCSV("key1;key2;key3;key4").build()).getResult();
        JsonNode json = JSONUtils.getInstance().parseObj(result);

        assertThat(json.path("key1").asText()).isEqualTo("aaaa");
        assertThat(json.path("key2").asText()).isEqualTo("bbbbb");
        assertThat(json.path("key3").asText()).isEqualTo("cccccc");
        assertThat(json.path("key4").asText()).isEqualTo("ddddddddd");
    }
}
