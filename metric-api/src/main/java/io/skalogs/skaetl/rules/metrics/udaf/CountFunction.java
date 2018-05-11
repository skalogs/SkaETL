package io.skalogs.skaetl.rules.metrics.udaf;

import lombok.Getter;

public class CountFunction extends AggregateFunction<Object,Double> {

    @Getter
    private Double count = 0d;

    @Override
    public AggregateFunction addValue(Object value) {
        count++;
        return this;
    }

    @Override
    public Double compute() {
        return count;
    }

    @Override
    public AggregateFunction<Object, Double> merge(AggregateFunction<Object, Double> newValue) {
        CountFunction countFunction = (CountFunction) newValue;
        count += countFunction.getCount();
        return this;
    }
}
