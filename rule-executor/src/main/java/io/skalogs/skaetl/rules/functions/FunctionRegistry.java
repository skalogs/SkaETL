package io.skalogs.skaetl.rules.functions;

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

import io.skalogs.skaetl.rules.domain.FilterFunctionDescription;
import io.skalogs.skaetl.rules.functions.numbers.*;
import io.skalogs.skaetl.rules.functions.strings.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FunctionRegistry {

    private Map<String, RuleFunction> registry = new HashMap<>();

    public FunctionRegistry() {
        register("IS_NUMBER", new IsNumberFunction());

        register("IS_BLANK", new IsBlankFunction());
        register("IS_NOT_BLANK", new IsNotBlankFunction());
        register("CONTAINS", new ContainsFunction());
        register("REGEXP", new RegexpFunction());
        register("MATCH", new RegexpFunction());

        register("IN", new InFunction());

        register("IN_SUBNET", new InSubnetFunction());

        register("ADD", new AddFunction());
        register("SUBTRACT", new SubtractFunction());
        register("MULTIPLY", new MultiplyFunction());
        register("DIVIDE", new DivideFunction());
        register("EXP", new ExpFunction());
    }

    public void register(String name, RuleFunction ruleFunction) {
        registry.put(name, ruleFunction);
    }

    public <T> T evaluate(String functionName, Object... args) {
        return (T) getRuleFunction(functionName).evaluate(args);
    }

    public RuleFunction getRuleFunction(String functionName) {
        return registry.get(functionName.toUpperCase());
    }

    public List<FilterFunctionDescription> filterFunctions() {
        return registry
                .entrySet()
                .stream()
                .map((e) -> new FilterFunctionDescription(e.getKey(),e.getValue().getDescription(),e.getValue().getExample()))
                .collect(Collectors.toList());
    }
}
