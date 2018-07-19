package io.skalogs.skaetl.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.service.ParserProcess;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class JsonStringParser  implements ParserProcess {
    @Override
    public ParserResult process(String value, ProcessParser processParser) {
        JsonNode asJsonNode= JSONUtils.getInstance().parse(value);
        return ParserResult.builder().result(asJsonNode.toString()).build();
    }
}
