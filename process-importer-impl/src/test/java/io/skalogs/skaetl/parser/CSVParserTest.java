package io.skalogs.skaetl.parser;

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
