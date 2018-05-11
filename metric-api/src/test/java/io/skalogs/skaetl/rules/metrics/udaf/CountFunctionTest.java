package io.skalogs.skaetl.rules.metrics.udaf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountFunctionTest {

    @Test
    public void shouldReturnZeroWhenNoValue() {
        CountFunction countFunction =new CountFunction();
        assertThat(countFunction.compute()).isEqualTo(0);
    }

    @Test
    public void shouldReturnCount() {
        CountFunction countFunction =new CountFunction();
        countFunction.addValue(100);
        countFunction.addValue(3);
        assertThat(countFunction.compute()).isEqualTo(2);
    }
}