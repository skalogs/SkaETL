package io.skalogs.skaetl.rules.metrics.udaf;


import static com.google.common.base.Preconditions.checkState;
import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

public class StdDevSampleFunction extends StdDevFunction {

    public final double sampleVariance() {
        checkState(getCount() > 1);
        if (isNaN(getSumOfSquaresOfDeltas())) {
            return NaN;
        }
        return ensureNonNegative(getSumOfSquaresOfDeltas()) / (getCount()- 1);
    }

    @Override
    public Double compute() {
        if (getCount() == 0) {
            return Double.NaN;
        }
        return Math.sqrt(sampleVariance());
    }
}
