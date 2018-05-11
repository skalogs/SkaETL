package io.skalogs.skaetl.rules.metrics.processor;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class MetricsSlackProcessorBeanFactory {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MetricsSlackProcessor slackProcessor(String webhook) {
        return new MetricsSlackProcessor(webhook, null);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MetricsSlackProcessor slackProcessor(String webhook, String template) {
        return new MetricsSlackProcessor(webhook, template);
    }
}
