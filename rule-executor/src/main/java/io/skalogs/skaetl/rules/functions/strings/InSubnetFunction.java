package io.skalogs.skaetl.rules.functions.strings;

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

import io.skalogs.skaetl.rules.functions.VarArgFilterFunction;
import io.skalogs.skaetl.utils.IPUtils;

import java.util.List;

public class InSubnetFunction extends VarArgFilterFunction<String> {
    public InSubnetFunction() {
        super("evaluates whether the string match an ip range or subnet", "myfield IN_SUBNET(\"10.2.0.0/24\")");
    }

    @Override
    public Boolean evaluateVarArgs(String ip, List<String> values) {
        if (ip == null) {
            return false;
        }
        for (String subnet : values) {
            if (IPUtils.isInSubnet(ip, subnet)) {
                return true;
            }
        }

        return false;
    }
}
