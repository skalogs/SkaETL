package io.skalogs.skaetl.rules.metrics;

/*-
 * #%L
 * metric-api
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


import io.skalogs.skaetl.domain.ProcessMetric;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import io.skalogs.skaetl.rules.codegeneration.metrics.RuleMetricToJava;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class RuleMetricExecutor {

    private final RuleMetricToJava ruleMetricToJava;
    private final FunctionRegistry functionRegistry;

    public GenericMetricProcessor instanciate(ProcessMetric processMetric) {
        return instanciate(ruleMetricToJava.convert(processMetric.getName(), processMetric.toDSL()), processMetric);
    }

    private GenericMetricProcessor instanciate(RuleCode ruleCode, ProcessMetric processMetric) {
        try {
            return instanciate(ruleCode.compile(), processMetric);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private GenericMetricProcessor instanciate(Class aClass, ProcessMetric processMetric) throws Exception {
        return (GenericMetricProcessor) aClass.getConstructor(ProcessMetric.class, FunctionRegistry.class).newInstance(processMetric, functionRegistry);
    }


}
