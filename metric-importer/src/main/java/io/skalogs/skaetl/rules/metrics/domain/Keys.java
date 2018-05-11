package io.skalogs.skaetl.rules.metrics.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class Keys {
    private final String ruleName;
    private final String ruleDSL;
    private final String project;
    private final Map<String, Object> keys = new HashMap<>();

    public void addKey(String name, Object value) {
        keys.put(name, value);
    }

    public Map<String, Object> getKeys() {
        return keys;
    }
}
