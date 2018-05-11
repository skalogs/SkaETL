package io.skalogs.skaetl.rules.metrics.udaf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SumFunctionTest {


    @Test
    public void shouldReturnZeroWhenNoValue() {
        SumFunction sumFunction =new SumFunction();
        assertThat(sumFunction.compute()).isEqualTo(0);
    }

    @Test
    public void shouldReturnSum() {
        SumFunction sumFunction =new SumFunction();
        sumFunction.addValue(97);
        sumFunction.addValue(3);
        assertThat(sumFunction.compute()).isEqualTo(100);
    }

}