package io.skalogs.skaetl.rules.metrics.udaf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MedianFunctionTest {

    @Test
    public void shouldComputeMedian() {
        MedianFunction function = new MedianFunction();
        function.addValue(1);
        function.addValue(2);
        function.addValue(3);
        function.addValue(4);
        function.addValue(5);
        function.addValue(6);
        function.addValue(100);
        assertThat(function.compute()).isEqualTo(4.0029296875);
    }
}