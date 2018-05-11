package io.skalogs.skaetl.rules.functions.strings;

import io.skalogs.skaetl.rules.functions.VarArgFilterFunction;

import java.util.List;

public class RegexpFunction extends VarArgFilterFunction<String> {
    @Override
    public Boolean evaluateVarArgs(String fieldValue, List<String> regexps) {
        return regexps.stream().filter(regexp -> fieldValue.matches(regexp)).findFirst().isPresent();
    }
}
