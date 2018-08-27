package io.skalogs.skaetl.service;


import io.skalogs.skaetl.domain.TypeParser;
import io.skalogs.skaetl.service.parser.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class DefaultParsers {

    public DefaultParsers(GenericParser genericParser, GrokParser grokParser, CEFParser cefParser, NitroParser nitroParser, CSVParser csvParser, JsonStringParser jsonStringParser) {
        genericParser.register(TypeParser.GROK,grokParser);
        genericParser.register(TypeParser.CEF,cefParser);
        genericParser.register(TypeParser.NITRO,nitroParser);
        genericParser.register(TypeParser.CSV,csvParser);
        genericParser.register(TypeParser.JSON_AS_STRING, jsonStringParser);
    }
}
