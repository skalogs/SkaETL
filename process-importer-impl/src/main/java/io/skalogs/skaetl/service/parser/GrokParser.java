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

import io.skalogs.skaetl.domain.GrokResult;
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.domain.TypeParser;
import io.skalogs.skaetl.service.GrokService;
import io.skalogs.skaetl.service.ParserProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class GrokParser extends ParserProcess {

    private final GrokService grokService;

    public GrokParser(GrokService grokService) {
        super(TypeParser.GROK, "Grok parser");
        this.grokService = grokService;
    }

    @Override
    public ParserResult process(String value, ProcessParser processParser) {
        GrokResult result = grokService.parseGrok(value, processParser.getGrokPattern());
        if (StringUtils.isBlank(result.value)) {
            return ParserResult.builder().failParse(true).messageFailParse(result.messageError).build();
        } else {
            return ParserResult.builder().result(result.value).build();
        }

    }

}
