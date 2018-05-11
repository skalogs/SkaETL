package io.skalogs.skaetl.rules.metrics.udaf;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class StdDevPopulationFunctionTest {

    @Test
    public void shouldReturnNaNIfNoResult() {
        StdDevPopulationFunction stdDevFunction = new StdDevPopulationFunction();
        stdDevFunction.compute();
    }

    @Test
    public void shouldStdDevMediumNumber() {
        StdDevPopulationFunction stdDevFunction = new StdDevPopulationFunction();
        stdDevFunction.addValue(600);
        stdDevFunction.addValue(470);
        stdDevFunction.addValue(170);
        stdDevFunction.addValue(430);
        stdDevFunction.addValue(300);
        assertThat(stdDevFunction.compute()).isCloseTo(147.32,within(0.01));
    }

    @Test
    public void shouldStdDevSmallNumber() {
        StdDevPopulationFunction stdDevFunction = new StdDevPopulationFunction();
        stdDevFunction.addValue(1);
        stdDevFunction.addValue(4);
        stdDevFunction.addValue(7);
        stdDevFunction.addValue(2);
        stdDevFunction.addValue(6);
        assertThat(stdDevFunction.compute()).isCloseTo(2.28,within(0.01));
    }

}