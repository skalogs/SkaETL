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
import io.skalogs.skaetl.rules.RuleFilterLexer;
import io.skalogs.skaetl.rules.RuleFilterParser;
import io.skalogs.skaetl.rules.codegeneration.RuleToJava;
import io.skalogs.skaetl.rules.codegeneration.SyntaxErrorListener;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import io.skalogs.skaetl.rules.codegeneration.exceptions.TemplatingException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.skalogs.skaetl.rules.codegeneration.RuleToJava.nullSafePredicate;

@Component
public class RuleFilterToJava {

    public RuleCode convert(String name, String dsl, ProcessFilter processFilter) {
        checkNotNull(name);
        checkNotNull(dsl);
        RuleFilterVisitorImpl ruleFilterVisitor = new RuleFilterVisitorImpl();
        ruleFilterVisitor.visit(parser(dsl).parse());
        try {
            return templating(name, dsl, ruleFilterVisitor, processFilter);
        } catch (Exception e) {
            throw new TemplatingException(e);
        }
    }




    private RuleCode templating(String name, String dsl, RuleFilterVisitorImpl ruleFilterVisitor,ProcessFilter processFilter) {
        String camelCaseName = RuleToJava.toCamelCase(name);
        String ruleClassName = StringUtils.replace(camelCaseName, "\"", "") + "Filter";
        String packageName = "io.skalogs.skaetl.rules.generated";
        String javaCode = "package " + packageName + ";\n" +
                "\n" +
                "import java.util.concurrent.*;\n" +
                "import static java.util.concurrent.TimeUnit.*;\n" +
                "\n" +
                "import static io.skalogs.skaetl.rules.UtilsValidator.*;\n" +
                "import javax.annotation.Generated;\n" +
                "import com.fasterxml.jackson.databind.JsonNode;\n" +
                "import io.skalogs.skaetl.domain.ProcessFilter;\n" +
                "import io.skalogs.skaetl.rules.filters.GenericFilter;\n" +
                "\n" +
                "/*\n" +
                dsl + "\n" +
                "*/\n" +
                "@Generated(\"etlFilter\")\n" +
                "public class " + ruleClassName + " extends GenericFilter {\n" +
                "    @Override\n" +
                "    public ProcessFilter getProcessFilter(){\n"+
                "        return ProcessFilter.builder().activeFailForward("+processFilter.getActiveFailForward()+").failForwardTopic(\""+processFilter.getFailForwardTopic()+"\").build();\n"+
                "    }\n"+
                "    @Override\n" +
                "    protected boolean doFilter(JsonNode jsonValue) {\n" +
                "        return " + nullSafePredicate(ruleFilterVisitor.getFilter()) + ";\n" +
                "    }\n" +
                "}";


        return new RuleCode(ruleClassName, dsl, packageName + "." + ruleClassName, javaCode);
    }

    public static RuleFilterParser parser(String dsl) {
        SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener(dsl);

        RuleFilterLexer lexer = new RuleFilterLexer(new ANTLRInputStream(dsl));
        lexer.removeErrorListeners();
        lexer.addErrorListener(syntaxErrorListener);

        RuleFilterParser parser = new RuleFilterParser(new CommonTokenStream(lexer));
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
        parser.removeErrorListeners();
        parser.addErrorListener(syntaxErrorListener);

        return parser;
    }

}
