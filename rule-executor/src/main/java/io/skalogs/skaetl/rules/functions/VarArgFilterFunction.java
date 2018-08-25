package io.skalogs.skaetl.rules.functions;

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

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class VarArgFilterFunction<InputType> extends FilterFunction<InputType> {
    public VarArgFilterFunction(String description, String example) {
        super(description, example);
    }

    @Override
    public Boolean evaluate(Object... args) {
        Preconditions.checkArgument(args.length >= 2);
        List<InputType> realArgs = Stream.of(args).skip(1).map(arg -> (InputType) arg).collect(Collectors.toList());
        return evaluateVarArgs((InputType) args[0], realArgs);
    }

    public abstract Boolean evaluateVarArgs(InputType fieldValue, List<InputType> values);
}
