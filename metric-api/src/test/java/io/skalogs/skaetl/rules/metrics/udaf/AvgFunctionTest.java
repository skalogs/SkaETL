package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AvgFunctionTest {

    @Test
    public void shouldReturnNaNWhenNoValue() {
        AvgFunction avgFunction =new AvgFunction();
        assertThat(avgFunction.compute()).isEqualTo(Double.NaN);
    }

    @Test
    public void shouldReturnAvg() {
        AvgFunction minFunction =new AvgFunction();
        minFunction.addValue(JsonNodeFactory.instance.numberNode(10));
        minFunction.addValue(JsonNodeFactory.instance.numberNode(0));
        assertThat(minFunction.compute()).isEqualTo(5);
    }
}