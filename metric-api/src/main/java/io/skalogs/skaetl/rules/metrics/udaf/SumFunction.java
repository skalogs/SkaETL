package io.skalogs.skaetl.rules.metrics.udaf;

import lombok.Getter;

public class SumFunction extends AggregateFunction<Number, Double> {
    @Getter
    private Double sum = 0d;

    @Override
    public AggregateFunction addValue(Number value) {
        double doubleValue = value.doubleValue();
        sum += doubleValue;
        return this;
    }

    @Override
    public Double compute() {
        return sum;
    }

    @Override
    public AggregateFunction<Number, Double> merge(AggregateFunction<Number, Double> newValue) {
        addValue(newValue.compute());
        return this;
    }
}
