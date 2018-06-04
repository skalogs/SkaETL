package io.skalogs.skaetl.rules.functions.numbers;

import io.skalogs.skaetl.rules.functions.TwoArgFunction;

public class ExpFunction extends TwoArgFunction<Number, Double> {

    public ExpFunction() {
        super("Returns the value of the first argument raised to the power of the second argument.", "EXP(mynumericField,2)");
    }

    @Override
    public Double twoArgFunction(Number arg1, Number arg2) {
        return Math.pow(arg1.doubleValue(), arg2.doubleValue());
    }
}
