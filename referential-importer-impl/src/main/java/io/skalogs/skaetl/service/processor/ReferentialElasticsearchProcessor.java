package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.domain.ESBuffer;
import io.skalogs.skaetl.domain.IndexShape;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
public class ReferentialElasticsearchProcessor extends AbstractElasticsearchProcessor<String, JsonNode> {

    private final ISO8601DateFormat df = new ISO8601DateFormat();
    private final RetentionLevel retentionLevel;

    public ReferentialElasticsearchProcessor(ESBuffer esBuffer, ESErrorRetryWriter esErrorRetryWriter, RetentionLevel retentionLevel) {
        super(esBuffer, esErrorRetryWriter);
        this.retentionLevel = retentionLevel;
    }

    @Override
    public void process(String key, JsonNode referential) {
        try {
            String valueAsString = referential.toString();
            String timestamp = referential.path("timestamp").asText();
            String id = referential.path("id").asText() + "-" + referential.path("key").asText() + "-" + referential.path("value").asText() + "-" + referential.path("project").asText() + "-" + referential.path("type").asText();
            processToElasticsearch(df.parse(timestamp), referential.path("project").asText(), referential.path("type").asText(), retentionLevel, IndexShape.daily, valueAsString, id);
        } catch (ParseException e) {
            log.error("Couldn't extract timestamp " + referential.toString(), e);
        }
    }
}