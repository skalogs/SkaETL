package io.skalogs.skaetl.rules.functions;

import com.google.common.base.Preconditions;

public abstract class TwoArgFunction<InputType, OutputType> extends RuleFunction<InputType, OutputType> {
    @Override
    public OutputType evaluate(Object... args) {
        Preconditions.checkArgument(args.length == 2);
        return twoArgFunction((InputType) args[0], (InputType) args[1]);
    }

    public abstract OutputType twoArgFunction(InputType arg1, InputType arg2);
}
