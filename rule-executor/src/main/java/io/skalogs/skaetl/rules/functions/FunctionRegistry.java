package io.skalogs.skaetl.rules.functions;

import io.skalogs.skaetl.rules.domain.FilterFunctionDescription;
import io.skalogs.skaetl.rules.functions.numbers.*;
import io.skalogs.skaetl.rules.functions.strings.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunctionRegistry {

    private static FunctionRegistry INSTANCE = new FunctionRegistry();

    private Map<String, RuleFunction> registry = new HashMap<>();

    private FunctionRegistry() {
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

    public static FunctionRegistry getInstance() {
        return INSTANCE;
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
