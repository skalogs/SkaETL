package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MedianFunctionTest {

    @Test
    public void shouldComputeMedian() {
        MedianFunction function = new MedianFunction();
        function.addValue(JsonNodeFactory.instance.numberNode(1));
        function.addValue(JsonNodeFactory.instance.numberNode(2));
        function.addValue(JsonNodeFactory.instance.numberNode(3));
        function.addValue(JsonNodeFactory.instance.numberNode(4));
        function.addValue(JsonNodeFactory.instance.numberNode(5));
        function.addValue(JsonNodeFactory.instance.numberNode(6));
        function.addValue(JsonNodeFactory.instance.numberNode(100));
        assertThat(function.compute()).isEqualTo(4.0029296875);
    }
}