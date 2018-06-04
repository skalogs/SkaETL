package io.skalogs.skaetl.rules.functions;

public abstract class FilterFunction<InputType> extends RuleFunction<InputType, Boolean> {
    public FilterFunction(String description, String example) {
        super(description, example);
    }
}
