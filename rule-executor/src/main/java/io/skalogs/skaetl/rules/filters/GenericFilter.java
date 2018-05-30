package io.skalogs.skaetl.rules.filters;

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
