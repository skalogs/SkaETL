package io.skalogs.skaetl.rules.metrics.processor;

import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.service.EmailService;
import io.skalogs.skaetl.utils.TemplateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.streams.processor.AbstractProcessor;

@AllArgsConstructor
@Slf4j
public class MetricsEmailProcessor extends AbstractProcessor<Keys, MetricResult> {

    private final String destinationMail;
    private final String template;
    private EmailService emailService;

    @Override
    public void process(Keys key, MetricResult value) {

        String v;

        if (!StringUtils.isBlank(template))
            v = TemplateUtils.getInstance().process(template, value.asMap());
        else
            v = value.toString();

        try {

            emailService.send(destinationMail, "SkETL message", v);

        } catch (Exception ex) {
            log.error("Exception during email sending {}", ex.getMessage());
            ex.printStackTrace();
        }

    }
}
