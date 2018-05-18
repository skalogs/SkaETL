package io.skalogs.skaetl.rules.functions.numbers;

import io.skalogs.skaetl.rules.functions.TwoArgFunction;

public class DivideFunction extends TwoArgFunction<Number,Double> {

    @Override
    public Double twoArgFunction(Number arg1, Number arg2) {
        return arg1.doubleValue() / arg2.doubleValue();
    }
}
