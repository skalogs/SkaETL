package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonNodeSlackProcessor extends AbstractSlackProcessor<String, JsonNode> {

    public JsonNodeSlackProcessor(String uri) {
        super(uri);
    }

    public JsonNodeSlackProcessor(String uri, String template) {
        super(uri, template);
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
