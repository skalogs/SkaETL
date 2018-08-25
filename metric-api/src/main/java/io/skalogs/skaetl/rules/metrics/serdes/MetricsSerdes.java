package io.skalogs.skaetl.rules.metrics.serdes;

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

import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class MetricsSerdes {

    public static Serde<Keys> keysSerde() {
        return Serdes.serdeFrom(new GenericSerializer<Keys>(), new GenericDeserializer(Keys.class));
    }

    public static Serde<AggregateFunction> aggFunctionSerdes() {
        return Serdes.serdeFrom(new GenericSerializer<AggregateFunction>(), new GenericDeserializer(AggregateFunction.class));
    }


    public static Serde<MetricResult> metricResultSerdes() {
        return Serdes.serdeFrom(new GenericSerializer<MetricResult>(), new GenericDeserializer(MetricResult.class));
    }
}
