package io.skalogs.skaetl.rules.functions;

import lombok.Getter;

@Getter
public abstract class RuleFunction<InputType, OutputType> {

    public abstract OutputType evaluate(Object... args);
}
