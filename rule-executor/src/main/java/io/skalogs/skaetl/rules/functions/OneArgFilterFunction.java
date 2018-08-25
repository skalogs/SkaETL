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

public abstract class OneArgFilterFunction<InputType> extends FilterFunction<InputType> {
    public OneArgFilterFunction(String description, String example) {
        super(description, example);
    }

    @Override
    public Boolean evaluate(Object... args) {
        Preconditions.checkArgument(args.length == 1);
        return evaluateOneArg((InputType) args[0]);
    }

    public abstract Boolean evaluateOneArg(InputType arg);
}
