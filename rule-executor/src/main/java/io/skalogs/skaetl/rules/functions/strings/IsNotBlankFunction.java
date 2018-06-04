package io.skalogs.skaetl.rules.functions.strings;

import io.skalogs.skaetl.rules.functions.OneArgFilterFunction;
import org.apache.commons.lang.StringUtils;

public class IsNotBlankFunction extends OneArgFilterFunction<String> {
    public IsNotBlankFunction() {
        super("return true if field is not null and not blank", "IS_NOT_BLANK(myfield)");
    }

    @Override
    public Boolean evaluateOneArg(String arg) {
        return StringUtils.isNotBlank(arg);
    }
}
