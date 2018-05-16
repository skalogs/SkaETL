package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.TypeOutput;
import io.skalogs.skaetl.service.SnmpService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSnmpProcessor<K, V> extends AbstractOutputProcessor<K, V> {

    private final SnmpService snmpService;

    public AbstractSnmpProcessor(SnmpService snmpService) {
        this.snmpService = snmpService;
    }

    protected abstract String buildMsg(V value);

    protected abstract JsonNode getMsg(V value);

    @Override
    public void process(K key, V value) {
        try {

            snmpService.send(buildMsg(value));

        } catch (Exception ex) {
            log.error("Exception during SNMP sending {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean support(TypeOutput typeOutput) {
        return typeOutput == TypeOutput.SNMP;
    }
}