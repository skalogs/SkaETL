package io.skalogs.skaetl.rules.functions.numbers;

/*-
 * #%L
 * rule-executor
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.skalogs.skaetl.rules.functions.TwoArgFunction;

public class AddFunction extends TwoArgFunction<Number,Double> {

    public AddFunction() {
        super("add two numbers", "myNumericField + myOtherNumericField");
    }

    @Override
    public Double twoArgFunction(Number arg1, Number arg2) {
        return arg1.doubleValue() + arg2.doubleValue();
    }
}
