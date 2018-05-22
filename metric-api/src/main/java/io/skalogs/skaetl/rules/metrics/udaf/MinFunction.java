package io.skalogs.skaetl.rules.metrics.udaf;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

public class MinFunction extends AggregateFunction<JsonNode, Double> {
    @Getter
    private Double minValue = Double.NaN;

    @Override
    public AggregateFunction addValue(JsonNode value) {
        double doubleValue = value.doubleValue();
        if (Double.isNaN(minValue)) {
            minValue = doubleValue;
        }
        if (doubleValue < minValue) {
            minValue = doubleValue;
        }
        return this;
    }

    @Override
    public Double compute() {
        return minValue;
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        return compute() < newValue.compute() ? this : newValue;
    }
}
