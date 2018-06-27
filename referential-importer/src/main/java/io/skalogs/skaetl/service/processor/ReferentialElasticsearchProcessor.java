package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.domain.ESBuffer;
import io.skalogs.skaetl.domain.Referential;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
public class ReferentialElasticsearchProcessor extends AbstractElasticsearchProcessor<String, Referential> {

    private final ISO8601DateFormat df = new ISO8601DateFormat();
    private final RetentionLevel retentionLevel;

    public ReferentialElasticsearchProcessor(ESBuffer esBuffer, ESErrorRetryWriter esErrorRetryWriter, RetentionLevel retentionLevel) {
        super(esBuffer, esErrorRetryWriter);
        this.retentionLevel = retentionLevel;
    }

    @Override
    public void process(String key, Referential referential) {
        try {
            String valueAsString = JSONUtils.getInstance().asJsonString(referential);
            String timestamp = referential.getTimestamp();
            String id = referential.getIdProcessReferential() + "-" + referential.getKey() + "-" + referential.getValue() + "-" + referential.getProject() + "-" + referential.getType();
            processToElasticsearch(df.parse(timestamp), referential.getProject(), referential.getType(), retentionLevel, valueAsString, id);
        } catch (JsonProcessingException e) {
            log.error("Couldn't transform value as referential " + key, e);
        } catch (ParseException e) {
            log.error("Couldn't extract timestamp " + referential.toString(), e);
        }
    }
}