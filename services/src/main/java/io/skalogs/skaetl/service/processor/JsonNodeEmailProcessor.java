package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.service.EmailService;

public class JsonNodeEmailProcessor extends AbstractEmailProcessor<String, JsonNode> {

    public JsonNodeEmailProcessor(String email, EmailService emailService) {
        super(email, emailService);
    }

    public JsonNodeEmailProcessor(String email, String template, EmailService emailService) {
        super(email, template, emailService);
    }

    @Override
    protected String buildMsg(JsonNode value) {
        return value.toString();
    }

    @Override
    protected JsonNode getMsg(JsonNode value) {
        return value;
    }
}