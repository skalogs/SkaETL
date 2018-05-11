package io.skalogs.skaetl.rules.functions;

import java.util.List;

public class InFunction<InputType> extends VarArgFilterFunction<InputType> {
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
