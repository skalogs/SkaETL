package io.skalogs.skaetl.rules.codegeneration.domain;

import io.skalogs.skaetl.compiler.DynamicCompiler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.substringBeforeLast;

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
