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


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.domain.TypeParser;
import io.skalogs.skaetl.service.ParserProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class NitroParser extends ParserProcess {
    private final static String NITRO = "McAfeeWG";

    public NitroParser() {
        super(TypeParser.NITRO, "Nitro parser");
    }

    @Override
    public ParserResult process(String value, ProcessParser processParser) {
        try {
            checkLineNitro(value);
            return ParserResult.builder().result(parse(value).toString()).build();
        } catch (Exception e) {
            return ParserResult.builder().failParse(true).messageFailParse("Parse Process Nitro Exception " + e.getMessage()).build();
        }

    }

    private JsonNode parse(String value) throws Exception {
        String nitro = value.substring(NITRO.length());
        Pattern pattern = Pattern.compile("[^|]*?=[^|]*");
        Matcher m = pattern.matcher(nitro);
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        while (m.find()) {
            String[] arraySplit = m.group().split("=");
            try {
                if (arraySplit.length > 1 && StringUtils.isNotBlank(arraySplit[0]) && arraySplit.length <= 2) {
                    String keyItem = arraySplit[0];
                    String valueItem = arraySplit[1];
                    json.put(keyItem, valueItem);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error("ArrayIndexOutOfBoundsException {}", e);
                throw new Exception("ArrayIndexOutOfBoundsException with Parse method {}", e);
            }
        }
        //if (json.length() == 0) {
        //    throw new Exception("Nitro Parser 0 out for value : " + value);
        //}
        return json;
    }

    private void checkLineNitro(String value) throws Exception {
        if (value == null) {
            throw new Exception("Nitro line null");
        }

        if (value.length() == 0) {
            throw new Exception("Nitro line empty");
        }

        if (StringUtils.isBlank(value)) {
            throw new Exception("Nitro line blank");
        }

        int indexNitro = value.indexOf(NITRO);
        if (indexNitro == -1) {
            throw new Exception("Nitro line not good format, doesn't start with " + NITRO + " value : " + value);
        }
    }
}
