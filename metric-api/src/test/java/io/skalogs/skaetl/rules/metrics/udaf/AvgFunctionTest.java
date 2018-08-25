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

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

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

    @Test
    public void toto() {
        System.out.println(OffsetDateTime.now().minusDays(30));
        System.out.println(OffsetDateTime.now().minusDays(30).with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS).toZonedDateTime());
    }
}
