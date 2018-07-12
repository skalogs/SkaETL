package io.skalogs.skaetl.rules.metrics.processor;

import io.skalogs.skaetl.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class MetricsEmailProcessorBeanFactory {

    private final EmailService emailService;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MetricsEmailProcessor emailProcessor(String destinationEmail, String template) {
        return new MetricsEmailProcessor(destinationEmail, template, emailService);
    }
}
