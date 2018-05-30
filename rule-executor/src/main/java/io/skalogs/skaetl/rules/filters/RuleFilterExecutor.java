package io.skalogs.skaetl.rules.filters;


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
