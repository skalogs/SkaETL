package io.skalogs.skaetl.rules.metrics.processor;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.service.processor.AbstractSlackProcessor;

public class MetricsSlackProcessor extends AbstractSlackProcessor<Keys, MetricResult> {

    public MetricsSlackProcessor(String uri, String template) {
        super(uri, template);
    }

    @Override
    protected String buildMsg(MetricResult value) {
        return value.toString();
    }

    @Override
    protected JsonNode getMsg(MetricResult value) {
        return value.asJsonNode();
    }

}
