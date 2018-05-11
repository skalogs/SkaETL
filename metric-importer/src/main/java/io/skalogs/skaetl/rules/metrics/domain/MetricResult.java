package io.skalogs.skaetl.rules.metrics.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.streams.kstream.Windowed;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class MetricResult {
    private final String ruleName;
    private final String ruleDSL;
    private final String project;
    private final Map<String, Object> keys;
    private final Date startDate;
    private final Date endDate;
    private final Date timestamp;
    private final Double result;
    @Wither
    private final JsonNode element;

    public MetricResult(Windowed<Keys> keysWindowed, Double result) {
        this.ruleName = keysWindowed.key().getRuleName();
        this.ruleDSL = keysWindowed.key().getRuleDSL();
        this.project = keysWindowed.key().getProject();
        this.keys = Collections.unmodifiableMap(keysWindowed.key().getKeys());
        this.startDate = new Date(keysWindowed.window().start());
        this.endDate = new Date(keysWindowed.window().end());
        //for Elasticsearch
        this.timestamp = endDate;
        this.result = result;
        this.element = null;

    }

    public Map<String, String> asMap() {

        Map<String, String> values = new HashMap<String, String>() {{
            put("result", getResult().toString());
            put("rule_dsl", getRuleDSL());
            put("rule_name", getRuleName());
            put("project", getProject());
        }};

        return values;
    }

    public JsonNode asJsonNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this.asMap(), JsonNode.class);
    }

    public String toString() {
        return StringUtils.join(new String[]{this.getRuleName(), " triggered with value ", this.getResult().toString()});
    }
}
