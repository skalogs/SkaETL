package io.skalogs.skaetl.rules.metrics.processor;

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
