package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.domain.ESBuffer;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
public class ReferentialElasticsearchProcessor  extends AbstractElasticsearchProcessor<String, JsonNode> {

    private final ISO8601DateFormat df = new ISO8601DateFormat();

    public ReferentialElasticsearchProcessor(ESBuffer esBuffer, ESErrorRetryWriter esErrorRetryWriter) {
        super(esBuffer, esErrorRetryWriter);
    }

    @Override
    public void process(String key, JsonNode jsonNode) {
        RetentionLevel retentionLevel = jsonNode.has("retention") ? RetentionLevel.valueOf(jsonNode.path("retention").asText()) : RetentionLevel.week;
        String valueAsString = jsonNode.toString();
        String timestamp = jsonNode.path("timestamp").asText();
        String id = jsonNode.path("idProcessReferential").asText()+"-"+jsonNode.path("key").asText()+"-"+jsonNode.path("value").asText()+"-"+jsonNode.path("project").asText()+"-"+jsonNode.path("type").asText();
        try {
            processToElasticsearch(df.parse(timestamp), jsonNode.path("project").asText(), jsonNode.path("type").asText(), retentionLevel, valueAsString, id);
        } catch (ParseException e) {
            log.error("Couldn't extract timestamp " + jsonNode.toString(), e);
        }
    }
}