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
import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.primitives.Doubles.isFinite;
import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

@Getter
public abstract class StdDevFunction extends AggregateFunction<JsonNode, Double> {
    private long count = 0;
    private Double mean = 0.0;
    private Double sumOfSquaresOfDeltas = 0.0;


    @Override
    public AggregateFunction addValue(JsonNode inputvalue) {
        Double value = inputvalue.doubleValue();
        if (count == 0) {
            count = 1;
            mean = value;
            if (!isFinite(value)) {
                sumOfSquaresOfDeltas = NaN;
            }
        } else {
            count++;
            if (isFinite(value) && isFinite(mean)) {
                Double delta = value - mean;
                mean += delta / count;
                sumOfSquaresOfDeltas += delta * (value - mean);
            } else {
                mean = calculateNewMeanNonFinite(mean, value);
                sumOfSquaresOfDeltas = NaN;
            }
        }
        return this;
    }

    private Double calculateNewMeanNonFinite(Double previousMean, Double value) {
        if (isFinite(previousMean)) {
            return value;
        } else if (isFinite(value) || previousMean == value) {
            return previousMean;
        } else {
            return NaN;
        }
    }

    protected Double ensureNonNegative(Double value) {
        checkArgument(!isNaN(value));
        if (value > 0.0) {
            return value;
        } else {
            return 0.0;
        }
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        return compute() > newValue.compute() ? this : newValue;
    }
}
