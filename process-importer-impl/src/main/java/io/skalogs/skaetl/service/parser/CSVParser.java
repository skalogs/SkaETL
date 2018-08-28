package io.skalogs.skaetl.service.parser;

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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.domain.TypeParser;
import io.skalogs.skaetl.service.ParserProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CSVParser extends ParserProcess {

    public CSVParser() {
        super(TypeParser.CSV, "CSV parser");
    }

    @Override
    public ParserResult process(String value, ProcessParser processParser) {
        String[] tabKey = processParser.getSchemaCSV().split(";");
        try {
            return ParserResult.builder().result(parse(value, tabKey)).build();
        } catch (Exception e) {
            return ParserResult.builder().failParse(true).messageFailParse("CSVParser Exception " + e.getMessage()).build();
        }
    }

    private String parse(String value, String[] tabKey) throws Exception {
        String[] tabValue = value.split(";");
        if (tabKey.length != tabValue.length) {
            throw new Exception("Size schema " + tabKey.length + " is different from Raw " + tabValue.length);
        }
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        for (int i = 0; i < tabValue.length; i++) {
            json.put(tabKey[i], tabValue[i]);
        }
        return json.toString();
    }
}
