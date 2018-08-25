package io.skalogs.skaetl.rules.metrics.domain;

/*-
 * #%L
 * metric-api
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;
import org.apache.commons.lang3.StringUtils;
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
