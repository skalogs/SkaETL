package io.skalogs.skaetl.rules.metrics.udaf;

/*-
 * #%L
 * metric-api
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
