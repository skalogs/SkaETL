package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

public class SumFunction extends AggregateFunction<JsonNode, Double> {
    @Getter
    private Double sum = 0d;

    @Override
    public AggregateFunction addValue(JsonNode value) {
        double doubleValue = value.doubleValue();
        sum += doubleValue;
        return this;
    }

    @Override
    public Double compute() {
        return sum;
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        sum += newValue.compute();
        return this;
    }
}
