package io.skalogs.skaetl.rules.metrics.processor;

import io.skalogs.skaetl.service.SnmpService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class MetricsSnmpProcessorBeanFactory {

    private final SnmpService snmpService;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MetricsSnmpProcessor snmpProcessor() {
        return new MetricsSnmpProcessor(snmpService);
    }
}
