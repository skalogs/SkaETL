package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MinFunctionTest {

    @Test
    public void shouldReturnNaNWhenNoValue() {
        MinFunction minFunction =new MinFunction();
        assertThat(minFunction.compute()).isEqualTo(Double.NaN);
    }

    @Test
    public void shouldReturnMin() {
        MinFunction minFunction =new MinFunction();
        minFunction.addValue(JsonNodeFactory.instance.numberNode(100));
        minFunction.addValue(JsonNodeFactory.instance.numberNode(3));
        assertThat(minFunction.compute()).isEqualTo(3);
    }

}