package io.skalogs.skaetl.rules.metrics.udaf;

import lombok.Getter;

public class AvgFunction extends AggregateFunction<Number, Double> {
    @Getter
    private Double sum = 0d;
    @Getter
    private Double count = 0d;

    @Override
    public AggregateFunction addValue(Number value) {
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
    public AggregateFunction<Number, Double> merge(AggregateFunction<Number, Double> newValue) {
        AvgFunction newValueFunction = (AvgFunction) newValue;
        sum += newValueFunction.sum;
        count += newValueFunction.count;
        return this;
    }
}
