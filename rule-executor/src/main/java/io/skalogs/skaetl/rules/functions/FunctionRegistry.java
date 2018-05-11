package io.skalogs.skaetl.rules.functions;

import io.skalogs.skaetl.rules.functions.numbers.IsNumberFunction;
import io.skalogs.skaetl.rules.functions.strings.*;

import java.util.HashMap;
import java.util.Map;

public class FunctionRegistry {

    private static FunctionRegistry INSTANCE = new FunctionRegistry();

    private Map<String, RuleFunction> registry = new HashMap<>();

    private FunctionRegistry() {
        register("IS_NUMBER", new IsNumberFunction());

        register("IS_BLANK", new IsBlankFunction());
        register("IS_NOT_BLANK", new IsNotBlank());
        register("CONTAINS", new ContainsFunction());
        register("REGEXP", new RegexpFunction());
        register("MATCH", new RegexpFunction());

        register("IN", new InFunction());

        register("IN_SUBNET", new InSubnetFunction());
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
}
