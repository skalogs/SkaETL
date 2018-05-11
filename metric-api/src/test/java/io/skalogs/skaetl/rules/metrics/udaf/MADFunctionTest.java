package io.skalogs.skaetl.rules.metrics.udaf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MADFunctionTest {

    @Test
    public void shouldReturnNanIfNoResult() {
        MADFunction madFunction = new MADFunction();
        assertThat(madFunction.compute()).isEqualTo(Double.NaN);
    }


    @Test
    public void shouldStdDevSmallNumber() {
        MADFunction madFunction = new MADFunction();
        madFunction.addValue(1);
        madFunction.addValue(2);
        madFunction.addValue(3);
        madFunction.addValue(4);
        madFunction.addValue(5);
        madFunction.addValue(6);
        madFunction.addValue(100);
        assertThat(madFunction.compute()).isEqualTo(2);
    }
}