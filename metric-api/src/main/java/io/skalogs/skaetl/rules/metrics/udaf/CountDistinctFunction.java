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

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class CountDistinctFunction extends AggregateFunction<Object, Double> {

    @Getter
    private Set<Object> uniqueElements = new HashSet<>();

    @Override
    public AggregateFunction addValue(Object value) {
        uniqueElements.add(value);
        return this;
    }

    @Override
    public Double compute() {
        return Double.valueOf(uniqueElements.size());
    }

    @Override
    public AggregateFunction<Object, Double> merge(AggregateFunction<Object, Double> newValue) {
        CountDistinctFunction countFunction = (CountDistinctFunction) newValue;
        uniqueElements.addAll(countFunction.getUniqueElements());
        return this;
    }
}
