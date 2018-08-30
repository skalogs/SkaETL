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

import io.skalogs.skaetl.rules.metrics.domain.AggFunctionDescription;
import io.skalogs.skaetl.rules.metrics.repository.AggFunctionDescriptionRepository;
import io.skalogs.skaetl.rules.metrics.udaf.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UDAFRegistry {

    private final AggFunctionDescriptionRepository aggFunctionDescriptionRepository;
    private Map<String, Class<? extends AggregateFunction>> registry = new HashMap<>();

    public UDAFRegistry(AggFunctionDescriptionRepository aggFunctionDescriptionRepository) {
        this.aggFunctionDescriptionRepository = aggFunctionDescriptionRepository;
        initDefaults();
    }

    public void initDefaults() {
        register("count", CountFunction.class, "Count number of occurrence of specified field. If the field is '*' then it will just count number of messages.");
        register("count-distinct", CountDistinctFunction.class, "Count distinct values of a specific field");
        register("sum", SumFunction.class, "Sum values of a specific field");
        register("min", MinFunction.class,"Minimum value of a specific field");
        register("max", MaxFunction.class, "Maximum value of a specific field");
        register("avg", AvgFunction.class, "Average value of a specific field");
        register("stddev", StdDevPopulationFunction.class, "Standard deviation of a specific field");
        register("stddev-sample", StdDevSampleFunction.class, "Standard deviation using sampling of a specific field");
        register("median", MedianFunction.class, "Median of a specific field");
    }

    public void register(String name, Class<? extends AggregateFunction> aggFunctionClass, String description) {
        registry.put(name.toLowerCase(), aggFunctionClass);
        aggFunctionDescriptionRepository.save(new AggFunctionDescription(name.toLowerCase(), description));
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
