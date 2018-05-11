package io.skalogs.skaetl.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(exclude = "original")
public class CEFEvent {
    private final String version;
    private final String deviceVendor;
    private final String deviceProduct;
    private final String deviceVersion;
    private final String signatureId;
    private final String name;
    private final String severity;
    private final DateTime timestamp;
    private final Map<String, String> extensions;

    public CEFEvent toNormalized() {
        return CEFEvent.builder()
                .version(version)
                .deviceVendor(deviceVendor)
                .deviceProduct(deviceProduct)
                .deviceVersion(deviceVersion)
                .signatureId(signatureId)
                .name(name)
                .severity(severity)
                .extensions(toNormalized(extensions))
                .timestamp(timestamp)
                .build();
    }

    private Map<String, String> toNormalized(Map<String, String> extensions) {
        Map<String, String> ret = new HashMap<>();
        for (Map.Entry<String, String> entry : extensions.entrySet()) {
            ret.put(CEFDictionary.toFullName(entry.getKey()), entry.getValue());
        }
        return ret;
    }
}

