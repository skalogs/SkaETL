package io.skalogs.skaetl.rules.metrics.udaf;

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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.skalogs.skaetl.rules.metrics.serdes.DoubleHistogramDeserializer;
import io.skalogs.skaetl.rules.metrics.serdes.DoubleHistogramSerializer;
import lombok.Getter;
import org.HdrHistogram.DoubleHistogram;

public class MedianFunction extends AggregateFunction<JsonNode, Double> {
    @Getter
    @JsonSerialize(using = DoubleHistogramSerializer.class)
    @JsonDeserialize(using = DoubleHistogramDeserializer.class)
    private DoubleHistogram histogram= new DoubleHistogram(3600000000000L, 3);

    @Override
    public AggregateFunction addValue(JsonNode value) {
        histogram.recordValue(value.doubleValue());
        return this;
    }

    @Override
    public Double compute() {
        return histogram.getValueAtPercentile(50);
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        return compute() > newValue.compute() ? this : newValue;
    }
}
