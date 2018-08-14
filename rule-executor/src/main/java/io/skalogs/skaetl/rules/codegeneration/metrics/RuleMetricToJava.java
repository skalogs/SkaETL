package io.skalogs.skaetl.rules.codegeneration.metrics;

import io.skalogs.skaetl.rules.RuleMetricLexer;
import io.skalogs.skaetl.rules.RuleMetricParser;
import io.skalogs.skaetl.rules.codegeneration.RuleToJava;
import io.skalogs.skaetl.rules.codegeneration.SyntaxErrorListener;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import io.skalogs.skaetl.rules.codegeneration.exceptions.TemplatingException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.skalogs.skaetl.rules.codegeneration.RuleToJava.nullSafePredicate;

@Component
public class RuleMetricToJava {

    public RuleCode convert(String name, String dsl) {
        checkNotNull(name);
        checkNotNull(dsl);
        RuleMetricVisitorImpl ruleMetricVisitor = new RuleMetricVisitorImpl();
        ruleMetricVisitor.visit(parser(dsl).parse());
        try {
            return templating(name, dsl, ruleMetricVisitor);
        } catch (Exception e) {
            throw new TemplatingException(e);
        }
    }

    private RuleCode templating(String name, String dsl, RuleMetricVisitorImpl ruleMetricVisitor) {
        String camelCaseName = RuleToJava.toCamelCase(name);
        String ruleClassName = StringUtils.replace(camelCaseName, "\"", "");
        String packageName = "io.skalogs.skaetl.metrics.generated";
        String javaCode = "package " + packageName + ";\n" +
                "\n" +
                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                "import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;\n" +
                "import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;\n" +
                "import io.skalogs.skaetl.domain.ProcessMetric;\n" +
                "import io.skalogs.skaetl.domain.JoinType;\n" +
                "import io.skalogs.skaetl.rules.metrics.domain.Keys;\n" +
                "import io.skalogs.skaetl.rules.metrics.domain.MetricResult;\n" +
                "import static java.util.concurrent.TimeUnit.*;\n" +
                "import io.skalogs.skaetl.utils.JSONUtils;\n" +
                "\n" +
                "import javax.annotation.Generated;\n" +
                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                "import static io.skalogs.skaetl.domain.JoinType.*;\n" +
                "import static io.skalogs.skaetl.domain.RetentionLevel.*;\n" +
                "\n" +
                "import org.apache.kafka.streams.kstream.*;\n" +
                "\n" +
                "/*\n" +
                dsl + "\n" +
                "*/\n" +
                "@Generated(\"etlMetric\")\n" +
                "public class " + ruleClassName + " extends GenericMetricProcessor {\n" +
                "    private final JSONUtils jsonUtils = JSONUtils.getInstance();\n" +
                "    public " + ruleClassName + "(ProcessMetric processMetric) {\n";
        if (StringUtils.isBlank(ruleMetricVisitor.getJoinFrom())) {
            javaCode += "        super(processMetric, \"" + ruleMetricVisitor.getFrom() + "\");\n";
        } else {
            javaCode += "        super(processMetric, \"" + ruleMetricVisitor.getFrom() + "\", \"" + ruleMetricVisitor.getJoinFrom() + "\");\n";
        }

        javaCode += "    }\n" +
                "    \n";
        if (ruleMetricVisitor.getJoinType() != null) {
            javaCode += "    @Override\n" +
                    "    protected JoinType joinType() {\n" +
                    "        return " + ruleMetricVisitor.getJoinType() + ";\n" +
                    "    }\n" +
                    "    \n";
        }
        javaCode +=
                "    @Override\n" +
                        "    protected AggregateFunction aggInitializer() {\n" +
                        "        return aggFunction(\"" + ruleMetricVisitor.getAggFunction() + "\");\n" +
                        "    }\n" +
                        "    \n" +
                        "    @Override\n" +
                        "    protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {\n" +
                        "        return " + ruleMetricVisitor.getWindow() + ";\n" +
                        "    }\n";
        if (StringUtils.isNotBlank(ruleMetricVisitor.getAggFunctionField())) {
            javaCode += "    \n" +
                    "    @Override\n" +
                    "    protected JsonNode mapValues(JsonNode value) {\n" +
                    "        return value.path(\"" + ruleMetricVisitor.getAggFunctionField() + "\");\n" +
                    "    }\n";
        }
        if (StringUtils.isNotBlank(ruleMetricVisitor.getWhere())) {
            javaCode += "    \n" +
                    "    @Override\n" +
                    "    protected boolean filter(String key, JsonNode jsonValue) {\n" +
                    "        return " + nullSafePredicate(ruleMetricVisitor.getWhere()) + ";\n" +
                    "    }\n";
        }

        if (StringUtils.isNotBlank(ruleMetricVisitor.getGroupBy())) {
            javaCode += "    \n" +
                    "    @Override\n" +
                    "    protected boolean filterKey(String key, JsonNode value) {\n";
            String[] keys = ruleMetricVisitor.getGroupBy().split(",");
            String filterKeyCode = Arrays.stream(keys)
                    .map(key -> "jsonUtils.has(value, \"" + key + "\")")
                    .collect(Collectors.joining(" && "));
            javaCode += "        return " + filterKeyCode + ";\n";
            javaCode += "    }\n" +
                    "    \n" +
                    "    @Override\n" +
                    "    protected Keys selectKey(String key, JsonNode value) {\n" +
                    "        Keys keys = super.selectKey(key,value);\n";
            for (String groupByField : keys) {
                javaCode += "        keys.addKey(\"" + groupByField + "\", jsonUtils.at(value, \"" + groupByField + "\").asText());\n";
            }
            javaCode += "        return keys;\n" +
                    "    }\n";
        }

        if (StringUtils.isNotBlank(ruleMetricVisitor.getHaving())) {
            javaCode += "    \n" +
                    "    @Override\n" +
                    "    protected boolean having(Windowed<Keys> keys, Double result) {\n" +
                    "        return " + nullSafePredicate(ruleMetricVisitor.getHaving()) + ";\n" +
                    "    }\n";
        }

        if (StringUtils.isNotBlank(ruleMetricVisitor.getJoinFrom())) {
            javaCode += "    \n" +
                    "    @Override\n" +
                    "    protected Keys selectKey(String key, JsonNode value) {\n" +
                    "        Keys keys = super.selectKey(key,value);\n" +
                    "        keys.addKey(\"" + ruleMetricVisitor.getJoinKeyFromA() + " = " + ruleMetricVisitor.getJoinKeyFromB() + "\", jsonUtils.at(value, \"" + ruleMetricVisitor.getJoinKeyFromA() + "\").asText());\n" +
                    "        return keys;\n" +
                    "    }\n" +
                    "    \n" +
                    "    @Override\n" +
                    "    protected Keys selectKeyJoin(String key, JsonNode value) {\n" +
                    "        Keys keys = super.selectKey(key,value);\n" +
                    "        keys.addKey(\"" + ruleMetricVisitor.getJoinKeyFromA() + " = " + ruleMetricVisitor.getJoinKeyFromB() + "\", jsonUtils.at(value, \"" + ruleMetricVisitor.getJoinKeyFromB() + "\").asText());\n" +
                    "        return keys;\n" +
                    "    }\n" +
                    "    \n" +
                    "    @Override\n" +
                    "    protected JoinWindows joinWindow() {\n" +
                    "        return " + ruleMetricVisitor.getJoinWindow() + ";\n" +
                    "    }\n";
        }

        if (StringUtils.isNotBlank(ruleMetricVisitor.getJoinWhere())) {
            javaCode += "    \n" +
                    "    @Override\n" +
                    "    protected boolean filterJoin(String key, JsonNode jsonNode) {\n" +
                    "        return " + nullSafePredicate(ruleMetricVisitor.getWhere()) + ";\n" +
                    "    }\n";
        }

        javaCode += "}";

        return new RuleCode(ruleClassName, dsl, packageName + "." + ruleClassName, javaCode);
    }

    public static RuleMetricParser parser(String dsl) {
        SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener(dsl);

        RuleMetricLexer lexer = new RuleMetricLexer(new ANTLRInputStream(dsl));
        lexer.removeErrorListeners();
        lexer.addErrorListener(syntaxErrorListener);

        RuleMetricParser parser = new RuleMetricParser(new CommonTokenStream(lexer));
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
        parser.removeErrorListeners();
        parser.addErrorListener(syntaxErrorListener);

        return parser;
    }

}
