package io.skalogs.skaetl.rules.metrics.udaf;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
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
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(600));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(470));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(170));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(430));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(300));
        assertThat(stdDevFunction.compute()).isCloseTo(147.32,within(0.01));
    }

    @Test
    public void shouldStdDevSmallNumber() {
        StdDevPopulationFunction stdDevFunction = new StdDevPopulationFunction();
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(1));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(4));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(7));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(2));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(6));
        assertThat(stdDevFunction.compute()).isCloseTo(2.28,within(0.01));
    }

}