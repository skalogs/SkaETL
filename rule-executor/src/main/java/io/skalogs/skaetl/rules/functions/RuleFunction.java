package io.skalogs.skaetl.rules.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class RuleFunction<InputType, OutputType> {

    private final String description;
    private final String example;

    public abstract OutputType evaluate(Object... args);
}
