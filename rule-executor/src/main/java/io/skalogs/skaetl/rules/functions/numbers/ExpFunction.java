package io.skalogs.skaetl.rules.functions.numbers;

import io.skalogs.skaetl.rules.functions.TwoArgFunction;

public class ExpFunction extends TwoArgFunction<Number, Double> {

    @Override
    public Double twoArgFunction(Number arg1, Number arg2) {
        return Math.pow(arg1.doubleValue(), arg2.doubleValue());
    }
}
