package io.skalogs.skaetl.rules.filters;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;

public abstract class GenericFilter {

    public boolean filter(JsonNode jsonValue) {
        if (jsonValue == null) {
            return false;
        }
        return doFilter(jsonValue);
    }

    protected abstract boolean doFilter(JsonNode jsonValue);

    protected boolean evaluate(String functionName, Object... args) {
        return FunctionRegistry.getInstance().evaluate(functionName, args);
    }

    protected Double evaluateOperation(String functionName, Object... args) {
        return FunctionRegistry.getInstance().evaluate(functionName, args);
    }

}
