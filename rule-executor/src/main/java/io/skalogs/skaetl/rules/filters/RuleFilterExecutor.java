package io.skalogs.skaetl.rules.filters;

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


import io.skalogs.skaetl.domain.ProcessFilter;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import io.skalogs.skaetl.rules.codegeneration.filters.RuleFilterToJava;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class RuleFilterExecutor {

    private final RuleFilterToJava ruleFilterToJava;

    public GenericFilter instanciate(String name, String dsl, ProcessFilter processFilter) {
        return instanciate(ruleFilterToJava.convert(name, dsl, processFilter));
    }

    private GenericFilter instanciate(RuleCode ruleCode) {
        try {
            return instanciate(ruleCode.compile());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private GenericFilter instanciate(Class aClass) throws Exception {
        return (GenericFilter) aClass.newInstance();
    }

}
