package io.skalogs.skaetl.rules.metrics.udaf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class StdDevSampleFunctionTest {


    @Test
    public void shouldReturnNanIfNoResult() {
        StdDevSampleFunction stdDevFunction = new StdDevSampleFunction();
        stdDevFunction.compute();
    }



    @Test
    public void shouldStdDevMediumNumber() {
        StdDevSampleFunction stdDevFunction = new StdDevSampleFunction();
        stdDevFunction.addValue(600);
        stdDevFunction.addValue(470);
        stdDevFunction.addValue(170);
        stdDevFunction.addValue(430);
        stdDevFunction.addValue(300);
        assertThat(stdDevFunction.compute()).isCloseTo(164.71,within(0.01));
    }


    @Test
    public void shouldStdDevSmallNumber() {
        StdDevSampleFunction stdDevFunction = new StdDevSampleFunction();
        stdDevFunction.addValue(1);
        stdDevFunction.addValue(4);
        stdDevFunction.addValue(7);
        stdDevFunction.addValue(2);
        stdDevFunction.addValue(6);
        assertThat(stdDevFunction.compute()).isCloseTo(2.54,within(0.01));
    }


}