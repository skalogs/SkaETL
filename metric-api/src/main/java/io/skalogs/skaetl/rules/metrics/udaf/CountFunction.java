package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

public class CountFunction extends AggregateFunction<JsonNode,Double> {

    @Getter
    private Double count = 0d;

    @Override
    public AggregateFunction addValue(JsonNode value) {
        count++;
        return this;
    }

    @Override
    public Double compute() {
        return count;
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        CountFunction countFunction = (CountFunction) newValue;
        count += countFunction.getCount();
        return this;
    }
}
