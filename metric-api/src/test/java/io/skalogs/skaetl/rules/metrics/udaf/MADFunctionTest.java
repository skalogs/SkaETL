package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MADFunctionTest {

    @Test
    public void shouldReturnNanIfNoResult() {
        MADFunction madFunction = new MADFunction();
        assertThat(madFunction.compute()).isEqualTo(Double.NaN);
    }


    @Test
    public void shouldStdDevSmallNumber() {
        MADFunction madFunction = new MADFunction();
        madFunction.addValue(JsonNodeFactory.instance.numberNode(1));
        madFunction.addValue(JsonNodeFactory.instance.numberNode(2));
        madFunction.addValue(JsonNodeFactory.instance.numberNode(3));
        madFunction.addValue(JsonNodeFactory.instance.numberNode(4));
        madFunction.addValue(JsonNodeFactory.instance.numberNode(5));
        madFunction.addValue(JsonNodeFactory.instance.numberNode(6));
        madFunction.addValue(JsonNodeFactory.instance.numberNode(100));
        assertThat(madFunction.compute()).isEqualTo(2);
    }
}