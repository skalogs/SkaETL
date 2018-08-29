package io.skalogs.skaetl.rules.metrics;

/*-
 * #%L
 * metric-importer
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

import io.skalogs.skaetl.rules.codegeneration.CodeGenerationUtils;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import io.skalogs.skaetl.rules.codegeneration.metrics.RuleMetricToJava;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import io.skalogs.skaetl.rules.repository.FilterFunctionDescriptionRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

public class GenericMetricProcessorCompileTest {
    private final FilterFunctionDescriptionRepository filterFunctionDescriptionRepositoryMock = Mockito.mock(FilterFunctionDescriptionRepository.class);
    private final FunctionRegistry functionRegistry = new FunctionRegistry(filterFunctionDescriptionRepositoryMock);
    @Test
    public void min() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES)";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }


    @Test
    public void filterWithFilter() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) WHERE type = \"something\"";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }


    @Test
    public void filterWithFilterFunction() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) WHERE type IN(\"something\",\"somethingelse\")";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }

    @Test
    public void filterWithFilterOperation() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) WHERE myfield = something + else";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }


    @Test
    public void groupBy() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) GROUP BY type";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }

    @Test
    public void having() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) HAVING result > 10";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }

    @Test
    public void countNoField() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT COUNT(*) FROM mytopic WINDOW TUMBLING(5 MINUTES) HAVING result > 10";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }

    @Test
    public void join() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) JOIN mytopic2 ON (userFromA, userFromB)  WINDOWED BY 10 MINUTES";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }


    @Test
    public void joinWithWhereClause() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) JOIN mytopic2 ON (userFromA, userFromB) WHERE ageDuCapitaine >= 42 WINDOWED BY 10 MINUTES";
        RuleCode rule = ruleToJava.convert("MyMinRule", dsl);
        rule.compile();
    }

    @Test
    @Ignore
    public void generateCode() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT MIN(duration) FROM mytopic WINDOW TUMBLING(5 MINUTES) JOIN mytopic2 ON (userFromA, userFromB)  WINDOWED BY 10 MINUTES";
        RuleCode myMetricRule = ruleToJava.convert("My_Min_Rule", dsl);
        File home = new File("target/generated-test-sources");
        CodeGenerationUtils.write(myMetricRule, home);
    }

    @Test
    @Ignore
    public void sss() {
        RuleMetricToJava ruleToJava = new RuleMetricToJava(functionRegistry);
        String dsl = "SELECT COUNT(*) FROM mytopic WINDOW TUMBLING(5 MINUTES) HAVING result > 10";
        RuleCode myMetricRule = ruleToJava.convert("My_Min_Rule", dsl);
        File home = new File("target/generated-test-sources");
        CodeGenerationUtils.write(myMetricRule, home);
    }
}
