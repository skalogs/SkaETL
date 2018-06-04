package io.skalogs.skaetl.rules.functions;

import java.util.List;

public class InFunction<InputType> extends VarArgFilterFunction<InputType> {
    public InFunction() {
        super("finds a match in the given arguments", "myfield in (\"a\",\"b\")");
    }

    @Override
    public Boolean evaluateVarArgs(InputType fieldValue, List<InputType> values) {
        if (fieldValue == null) {
            return false;
        }
        for (InputType value : values) {
            if (fieldValue.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
