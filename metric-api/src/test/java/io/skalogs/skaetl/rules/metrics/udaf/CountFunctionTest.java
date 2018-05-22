package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
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
        countFunction.addValue(JsonNodeFactory.instance.numberNode(100));
        countFunction.addValue(JsonNodeFactory.instance.numberNode(3));
        assertThat(countFunction.compute()).isEqualTo(2);
    }
}