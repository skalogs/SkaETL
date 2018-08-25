package io.skalogs.skaetl.rules.codegeneration.domain;

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

import io.skalogs.skaetl.compiler.DynamicCompiler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

@AllArgsConstructor
@Data
@Slf4j
public class RuleCode {
    public final String name;
    public final String dsl;
    public final String ruleClassName;
    public final String java;

    public String toFilename() {
        return substringBeforeLast(ruleClassName, ".").replace(".", "/")
                + "/"
                + substringAfterLast(ruleClassName, ".")
                + ".java";
    }

    public Class compile() {

        log.debug("compiling {}", name);
        DynamicCompiler dynamicCompiler = new DynamicCompiler();
        dynamicCompiler.addSource(ruleClassName, java);
        Map<String, Class<?>> compiled = dynamicCompiler.build();
        return compiled.get(ruleClassName);

    }
}
