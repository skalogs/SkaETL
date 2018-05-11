package io.skalogs.skaetl.rules.functions;

import com.google.common.base.Preconditions;

public abstract class OneArgFilterFunction<InputType> extends FilterFunction<InputType> {
    @Override
    public Boolean evaluate(Object... args) {
        Preconditions.checkArgument(args.length == 1);
        return evaluateOneArg((InputType) args[0]);
    }

    public abstract Boolean evaluateOneArg(InputType arg);
}
