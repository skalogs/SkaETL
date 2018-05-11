package io.skalogs.skaetl.rules.functions;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class VarArgFilterFunction<InputType> extends FilterFunction<InputType> {
    @Override
    public Boolean evaluate(Object... args) {
        Preconditions.checkArgument(args.length >= 2);
        List<InputType> realArgs = Stream.of(args).skip(1).map(arg -> (InputType) arg).collect(Collectors.toList());
        return evaluateVarArgs((InputType) args[0], realArgs);
    }

    public abstract Boolean evaluateVarArgs(InputType fieldValue, List<InputType> values);
}
