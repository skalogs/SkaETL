package io.skalogs.skaetl.rules.metrics.udaf;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class CountDistinctFunction extends AggregateFunction<Object, Double> {

    @Getter
    private Set<Object> uniqueElements = new HashSet<>();

    @Override
    public AggregateFunction addValue(Object value) {
        uniqueElements.add(value);
        return this;
    }

    @Override
    public Double compute() {
        return Double.valueOf(uniqueElements.size());
    }

    @Override
    public AggregateFunction<Object, Double> merge(AggregateFunction<Object, Double> newValue) {
        CountDistinctFunction countFunction = (CountDistinctFunction) newValue;
        uniqueElements.addAll(countFunction.getUniqueElements());
        return this;
    }
}
