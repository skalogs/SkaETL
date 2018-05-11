package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.TypeOutput;
import io.skalogs.skaetl.service.EmailService;
import io.skalogs.skaetl.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public abstract class AbstractEmailProcessor<K, V> extends AbstractOutputProcessor<K, V> {

    private final String email;
    private final String template;
    private final EmailService emailService;

    public AbstractEmailProcessor(String email, EmailService emailService) {
        this.email = email;
        this.emailService = emailService;
        this.template = null;
    }

    public AbstractEmailProcessor(String email, String template, EmailService emailService) {
        this.email = email;
        this.template = template;
        this.emailService = emailService;
    }

    protected abstract String buildMsg(V value);

    protected abstract JsonNode getMsg(V value);

    @Override
    public void process(K key, V value) {
        try {

            String v;

            if (!StringUtils.isBlank(template))
                v = TemplateUtils.getInstance().process(template, getMsg(value));
            else
                v = getMsg(value).toString();

            emailService.send(email, "SkETL message", v);

        } catch (Exception ex) {
            log.error("Exception during email sending {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean support(TypeOutput typeOutput) {
        return typeOutput == TypeOutput.EMAIL;
    }
}