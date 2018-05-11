package io.skalogs.skaetl.rules.metrics.udaf;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.primitives.Doubles.isFinite;
import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

@Getter
public abstract class StdDevFunction extends AggregateFunction<Number, Double> {
    private long count = 0;
    private Double mean = 0.0;
    private Double sumOfSquaresOfDeltas = 0.0;


    @Override
    public AggregateFunction addValue(Number inputvalue) {
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
    public AggregateFunction<Number, Double> merge(AggregateFunction<Number, Double> newValue) {
        return compute() > newValue.compute() ? this : newValue;
    }
}
