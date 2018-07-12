package io.skalogs.skaetl.rules.metrics.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.domain.ESBuffer;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import io.skalogs.skaetl.service.processor.AbstractElasticsearchProcessor;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricsElasticsearchProcessor extends AbstractElasticsearchProcessor<Keys, MetricResult> {

    private final ISO8601DateFormat dateFormat = new ISO8601DateFormat();
    private final RetentionLevel retentionLevel;

    public MetricsElasticsearchProcessor(ESBuffer esBuffer, ESErrorRetryWriter esErrorRetryWriter, RetentionLevel retention) {
        super(esBuffer, esErrorRetryWriter);
        retentionLevel = retention;
    }

    @Override
    public void process(Keys key, MetricResult value) {
        try {
            String valueAsString = JSONUtils.getInstance().asJsonString(value);
            String metricId = value.getRuleName() + value.getTimestamp() + value.getKeys().hashCode();
            if (value.getElement() != null) {
                metricId += value.getElement().toString();
            }
            processToElasticsearch(value.getTimestamp(), value.getProject(), "metrics", retentionLevel, valueAsString, metricId);
        } catch (JsonProcessingException e) {
            log.error("Couldn't transform value as metric " + key, e);
        }
    }
}
