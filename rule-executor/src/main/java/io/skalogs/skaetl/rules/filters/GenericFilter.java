package io.skalogs.skaetl.rules.filters;

/*-
 * #%L
 * rule-executor
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

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.FilterResult;
import io.skalogs.skaetl.domain.ProcessFilter;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;

public abstract class GenericFilter {

    public FilterResult filter(JsonNode jsonValue) {
        if (jsonValue == null) {
           return null;
        }
        return FilterResult.builder()
                .filter(doFilter(jsonValue))
                .processFilter(getProcessFilter())
                .build();
    }

    protected abstract boolean doFilter(JsonNode jsonValue);

    protected abstract ProcessFilter getProcessFilter();

    protected boolean evaluate(String functionName, Object... args) {
        return FunctionRegistry.getInstance().evaluate(functionName, args);
    }

    protected Double evaluateOperation(String functionName, Object... args) {
        return FunctionRegistry.getInstance().evaluate(functionName, args);
    }

}
