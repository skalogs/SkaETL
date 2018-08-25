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
import com.google.common.math.Quantiles;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MADFunction extends AggregateFunction<JsonNode, Double> {
    @Getter
    private List<Double> values = new ArrayList<>();

    @Override
    public AggregateFunction addValue(JsonNode value) {
        values.add(value.doubleValue());
        return this;
    }

    @Override
    public Double compute() {
        if (values.isEmpty()) {
            return Double.NaN;
        }
        return mad();
    }

    private Double mad() {
        double median = Quantiles.median().compute(values);
        List<Double> deviations = deviation(values, median);
        return Quantiles.median().compute(deviations);

    }

    private List<Double> deviation(List<Double> values, Double median) {
        return values.stream()
                .map(value -> Math.abs(value - median))
                .collect(Collectors.toList());
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        return compute() > newValue.compute() ? this : newValue;
    }
}
