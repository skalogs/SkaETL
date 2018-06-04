package io.skalogs.skaetl.rules.functions.strings;

import io.skalogs.skaetl.rules.functions.VarArgFilterFunction;

import java.util.List;

public class RegexpFunction extends VarArgFilterFunction<String> {
    public RegexpFunction() {
        super("evaluates whether a field match regexps", "myfield REGEXP(\"myregex\")");
    }

    @Override
    public Boolean evaluateVarArgs(String fieldValue, List<String> regexps) {
        return regexps.stream().filter(regexp -> fieldValue.matches(regexp)).findFirst().isPresent();
    }
}
