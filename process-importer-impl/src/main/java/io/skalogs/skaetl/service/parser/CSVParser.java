package io.skalogs.skaetl.service.parser;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.service.ParserProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CSVParser implements ParserProcess {

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
