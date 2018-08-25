package io.skalogs.skaetl.rules.metrics;

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

import io.skalogs.skaetl.rules.metrics.udaf.*;

import java.util.HashMap;
import java.util.Map;

public class UDAFRegistry {

    private static UDAFRegistry INSTANCE = new UDAFRegistry();

    private Map<String, Class<? extends AggregateFunction>> registry = new HashMap<>();

    private UDAFRegistry() {
        register("count", CountFunction.class);
        register("count-distinct", CountDistinctFunction.class);
        register("sum", SumFunction.class);
        register("min", MinFunction.class);
        register("max", MaxFunction.class);
        register("avg", AvgFunction.class);
        register("stddev", StdDevPopulationFunction.class);
        register("stddev-sample", StdDevSampleFunction.class);
        register("median", MedianFunction.class);
    }

    public void register(String name, Class<? extends AggregateFunction> aggFunctionClass) {
        registry.put(name.toLowerCase(), aggFunctionClass);
    }

    public static UDAFRegistry getInstance() {
        return INSTANCE;
    }

    public AggregateFunction get(String aggregateFunctionName) {
        try {
            Class<? extends AggregateFunction> aggFunctionClass = registry.get(aggregateFunctionName.toLowerCase());
            return aggFunctionClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
