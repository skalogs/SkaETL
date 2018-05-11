package io.skalogs.skaetl.rules.metrics;


import io.skalogs.skaetl.domain.ProcessMetric;
import io.skalogs.skaetl.rules.codegeneration.domain.RuleCode;
import io.skalogs.skaetl.rules.codegeneration.metrics.RuleMetricToJava;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class RuleMetricExecutor {

    private final RuleMetricToJava ruleMetricToJava;

    public GenericMetricProcessor instanciate(ProcessMetric processMetric) {
        return instanciate(ruleMetricToJava.convert(processMetric.getName(), processMetric.toDSL()),processMetric);
    }

    private GenericMetricProcessor instanciate(RuleCode ruleCode, ProcessMetric processMetric) {
        try {
            return instanciate(ruleCode.compile(),processMetric);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private GenericMetricProcessor instanciate(Class aClass,ProcessMetric processMetric) throws Exception {
        return (GenericMetricProcessor) aClass.getConstructor(ProcessMetric.class).newInstance(processMetric);
    }


}
