package io.skalogs.skaetl.rules.codegeneration.filters;

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
import io.skalogs.skaetl.rules.codegeneration.CodeGenerationUtils;
import io.skalogs.skaetl.rules.codegeneration.SyntaxErrorListener;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleFilterToJavaTest {

    private final FunctionRegistry functionRegistry = new FunctionRegistry();

    @Test
    public void checkJavaClassName() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava(functionRegistry);
        String dsl = "myfield >=3";
        RuleCode rule = ruleToJava.convert("my simple rule", dsl,ProcessFilter.builder().build());
        assertThat(rule.getName()).isEqualTo("MySimpleRuleFilter");
        assertThat(rule.getRuleClassName()).isEqualTo("io.skalogs.skaetl.rules.generated.MySimpleRuleFilter");
    }

    @Test
    public void simple() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava(functionRegistry);
        String dsl = "myfield >=3";
        Boolean activeFailForward = true;
        String failForwardTopic= "topicTest";
        RuleCode rule = ruleToJava.convert("Simple", dsl, ProcessFilter.builder().activeFailForward(activeFailForward).failForwardTopic(failForwardTopic).build());
        assertThat(rule)
                .isEqualTo(new RuleCode("SimpleFilter",
                        dsl,
                        "io.skalogs.skaetl.rules.generated.SimpleFilter",
                        "package io.skalogs.skaetl.rules.generated;\n" +
                                "\n" +
                                "import java.util.concurrent.*;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import javax.annotation.Generated;\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.domain.ProcessFilter;\n" +
                                "import io.skalogs.skaetl.rules.filters.GenericFilter;\n" +
                                "import io.skalogs.skaetl.rules.functions.FunctionRegistry;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlFilter\")\n" +
                                "public class SimpleFilter extends GenericFilter {\n" +
                                "    public SimpleFilter(FunctionRegistry functionRegistry) {\n" +
                                "        super(functionRegistry);\n" +
                                "    }\n"+
                                "    @Override\n" +
                                "    public ProcessFilter getProcessFilter(){\n"+
                                "        return ProcessFilter.builder().activeFailForward("+activeFailForward+").failForwardTopic(\""+failForwardTopic+"\").build();\n"+
                                "    }\n"+
                                "    @Override\n" +
                                "    protected boolean doFilter(JsonNode jsonValue) {\n" +
                                "        return isGreaterThanOrEqual(get(jsonValue,\"myfield\"),3);\n" +
                                "    }\n" +
                                "}"));
        rule.compile();
    }

    @Test
    public void mutipleConditions() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava(functionRegistry);
        String dsl = "myfield >=3 AND toto = \"something\"";
        Boolean activeFailForward = true;
        String failForwardTopic= "topicTest";
        RuleCode rule = ruleToJava.convert("Multiple_Conditions", dsl, ProcessFilter.builder().activeFailForward(activeFailForward).failForwardTopic(failForwardTopic).build());
        assertThat(rule)
                .isEqualTo(new RuleCode("MultipleConditionsFilter",
                        dsl,
                        "io.skalogs.skaetl.rules.generated.MultipleConditionsFilter",
                        "package io.skalogs.skaetl.rules.generated;\n" +
                                "\n" +
                                "import java.util.concurrent.*;\n" +
                                "import static java.util.concurrent.TimeUnit.*;\n" +
                                "\n" +
                                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                                "import javax.annotation.Generated;\n" +
                                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                                "import io.skalogs.skaetl.domain.ProcessFilter;\n" +
                                "import io.skalogs.skaetl.rules.filters.GenericFilter;\n" +
                                "import io.skalogs.skaetl.rules.functions.FunctionRegistry;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlFilter\")\n" +
                                "public class MultipleConditionsFilter extends GenericFilter {\n" +
                                "    public MultipleConditionsFilter(FunctionRegistry functionRegistry) {\n" +
                                "        super(functionRegistry);\n" +
                                "    }\n"+
                                "    @Override\n" +
                                "    public ProcessFilter getProcessFilter(){\n"+
                                "        return ProcessFilter.builder().activeFailForward("+activeFailForward+").failForwardTopic(\""+failForwardTopic+"\").build();\n"+
                                "    }\n"+
                                "    @Override\n" +
                                "    protected boolean doFilter(JsonNode jsonValue) {\n" +
                                "        return isGreaterThanOrEqual(get(jsonValue,\"myfield\"),3) && isEqualTo(get(jsonValue,\"toto\"),\"something\");\n" +
                                "    }\n" +
                                "}"));
        rule.compile();
    }

    @Test(expected = SyntaxErrorListener.SyntaxException.class)
    public void wrongSyntax() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava(functionRegistry);
        String dsl = "UNKNOWNFUNCTION(myfield, anotherfield)";
        ruleToJava.convert("MyMinRule", dsl,ProcessFilter.builder().build());
    }

    @Test
    @Ignore
    public void generateCode() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava(functionRegistry);
        String dsl = "myfield >=3 AND toto = \"something\"";
        RuleCode multipleConditions = ruleToJava.convert("MultipleConditions", dsl,ProcessFilter.builder().build());
        File home = new File("target/generated-test-sources");
        CodeGenerationUtils.write(multipleConditions, home);
    }

}
