package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

public class AvgFunction extends AggregateFunction<JsonNode, Double> {
    @Getter
    private Double sum = 0d;
    @Getter
    private Double count = 0d;

    @Override
    public AggregateFunction addValue(JsonNode value) {
        double doubleValue = value.doubleValue();
        sum += doubleValue;
        count++;
        return this;
    }

    @Override
    public Double compute() {
        return sum / count;
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        AvgFunction newValueFunction = (AvgFunction) newValue;
        sum += newValueFunction.sum;
        count += newValueFunction.count;
        return this;
    }
}
