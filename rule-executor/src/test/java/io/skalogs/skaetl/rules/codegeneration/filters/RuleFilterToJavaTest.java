package io.skalogs.skaetl.rules.codegeneration.filters;

import io.skalogs.skaetl.rules.codegeneration.CodeGenerationUtils;
import io.skalogs.skaetl.rules.codegeneration.SyntaxErrorListener;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleFilterToJavaTest {

    @Test
    public void checkJavaClassName() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava();
        String dsl = "myfield >=3";
        RuleCode rule = ruleToJava.convert("my simple rule", dsl);
        assertThat(rule.getName()).isEqualTo("MySimpleRuleFilter");
        assertThat(rule.getRuleClassName()).isEqualTo("io.skalogs.skaetl.rules.generated.MySimpleRuleFilter");
    }

    @Test
    public void simple() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava();
        String dsl = "myfield >=3";
        RuleCode rule = ruleToJava.convert("Simple", dsl);
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
                                "import io.skalogs.skaetl.rules.filters.GenericFilter;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlFilter\")\n" +
                                "public class SimpleFilter extends GenericFilter {\n" +
                                "    @Override\n" +
                                "    protected boolean doFilter(JsonNode jsonValue) {\n" +
                                "        return isGreaterThanOrEqual(get(jsonValue,\"myfield\"),3);\n" +
                                "    }\n" +
                                "}"));
        rule.compile();
    }

    @Test
    public void mutipleConditions() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava();
        String dsl = "myfield >=3 AND toto = \"something\"";
        RuleCode rule = ruleToJava.convert("Multiple_Conditions", dsl);
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
                                "import io.skalogs.skaetl.rules.filters.GenericFilter;\n" +
                                "\n" +
                                "/*\n" +
                                dsl + "\n" +
                                "*/\n" +
                                "@Generated(\"etlFilter\")\n" +
                                "public class MultipleConditionsFilter extends GenericFilter {\n" +
                                "    @Override\n" +
                                "    protected boolean doFilter(JsonNode jsonValue) {\n" +
                                "        return isGreaterThanOrEqual(get(jsonValue,\"myfield\"),3) && isEqualTo(get(jsonValue,\"toto\"),\"something\");\n" +
                                "    }\n" +
                                "}"));
        rule.compile();
    }

    @Test(expected = SyntaxErrorListener.SyntaxException.class)
    public void wrongSyntax() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava();
        String dsl = "UNKNOWNFUNCTION(myfield, anotherfield)";
        ruleToJava.convert("MyMinRule", dsl);
    }

    @Test
    @Ignore
    public void generateCode() {
        RuleFilterToJava ruleToJava = new RuleFilterToJava();
        String dsl = "myfield >=3 AND toto = \"something\"";
        RuleCode multipleConditions = ruleToJava.convert("MultipleConditions", dsl);
        File home = new File("target/generated-test-sources");
        CodeGenerationUtils.write(multipleConditions, home);
    }

}