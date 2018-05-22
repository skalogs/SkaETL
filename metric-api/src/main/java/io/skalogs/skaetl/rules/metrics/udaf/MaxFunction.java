package io.skalogs.skaetl.rules.metrics.udaf;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

public class MaxFunction extends AggregateFunction<JsonNode, Double> {
    @Getter
    private Double maxValue = Double.NaN;

    @Override
    public AggregateFunction addValue(JsonNode value) {
        double doubleValue = value.doubleValue();
        if (Double.isNaN(maxValue)) {
            maxValue = doubleValue;
        }
        if (doubleValue > maxValue) {
            maxValue = doubleValue;
        }
        return this;
    }

    @Override
    public Double compute() {
        return maxValue;
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        return compute() > newValue.compute() ? this : newValue;
    }
}
