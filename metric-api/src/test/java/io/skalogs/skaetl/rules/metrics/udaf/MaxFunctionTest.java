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

public class MaxFunctionTest {

    @Test
    public void shouldReturnNaNWhenNoValue() {
        MaxFunction maxFunction =new MaxFunction();
        assertThat(maxFunction.compute()).isEqualTo(Double.NaN);
    }


    @Test
    public void shouldReturnMax() {
        MaxFunction maxFunction =new MaxFunction();
        maxFunction.addValue(JsonNodeFactory.instance.numberNode(100));
        maxFunction.addValue(JsonNodeFactory.instance.numberNode(3));
        assertThat(maxFunction.compute()).isEqualTo(100);
    }

}
