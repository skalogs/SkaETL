package io.skalogs.skaetl.service;


import io.skalogs.skaetl.service.parser.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class DefaultParsers {

    public DefaultParsers(GenericParser genericParser, GrokParser grokParser, CEFParser cefParser, NitroParser nitroParser, CSVParser csvParser, JsonStringParser jsonStringParser) {
        genericParser.register(grokParser);
        genericParser.register(cefParser);
        genericParser.register(nitroParser);
        genericParser.register(csvParser);
        genericParser.register(jsonStringParser);
    }
}
