package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.service.SnmpService;

public class JsonNodeSnmpProcessor extends AbstractSnmpProcessor<String, JsonNode> {

    public JsonNodeSnmpProcessor(SnmpService snmpService) {
        super(snmpService);
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