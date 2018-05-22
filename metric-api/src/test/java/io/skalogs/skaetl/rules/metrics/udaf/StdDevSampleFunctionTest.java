package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
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
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(600));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(470));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(170));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(430));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(300));
        assertThat(stdDevFunction.compute()).isCloseTo(164.71,within(0.01));
    }


    @Test
    public void shouldStdDevSmallNumber() {
        StdDevSampleFunction stdDevFunction = new StdDevSampleFunction();
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(1));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(4));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(7));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(2));
        stdDevFunction.addValue(JsonNodeFactory.instance.numberNode(6));
        assertThat(stdDevFunction.compute()).isCloseTo(2.54,within(0.01));
    }


}