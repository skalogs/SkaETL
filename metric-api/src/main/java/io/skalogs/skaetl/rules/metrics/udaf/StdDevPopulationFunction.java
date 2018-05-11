package io.skalogs.skaetl.rules.metrics.udaf;


import static com.google.common.base.Preconditions.checkState;
import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

public class StdDevPopulationFunction extends StdDevFunction {
    private Double populationVariance() {
        checkState(getCount() != 0);
        if (isNaN(getSumOfSquaresOfDeltas())) {
            return NaN;
        }
        if (getCount() == 1) {
            return 0.0;
        }
        return ensureNonNegative(getSumOfSquaresOfDeltas()) / getCount();
    }

    @Override
    public Double compute() {
        if (getCount() == 0) {
            return Double.NaN;
        }
        return Math.sqrt(populationVariance());
    }
}
