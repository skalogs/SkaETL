package io.skalogs.skaetl.rules.functions.numbers;

import io.skalogs.skaetl.rules.functions.OneArgFilterFunction;
import org.apache.commons.lang.StringUtils;

public class IsNumberFunction extends OneArgFilterFunction<Number> {
    public IsNumberFunction() {
        super("return true if field is a number", "IS_NUMBER(myfield)");
    }

    @Override
    public Boolean evaluateOneArg(Number arg) {
        return StringUtils.isNumeric(arg.toString());
    }
}
