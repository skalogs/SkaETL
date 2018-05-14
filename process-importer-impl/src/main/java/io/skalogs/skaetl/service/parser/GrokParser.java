package io.skalogs.skaetl.service.parser;

import io.skalogs.skaetl.domain.GrokResult;
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.service.GrokService;
import io.skalogs.skaetl.service.ParserProcess;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@AllArgsConstructor
public class GrokParser implements ParserProcess {

    private GrokService grokService;

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
