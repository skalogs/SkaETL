package io.skalogs.skaetl.domain;

/*-
 * #%L
 * process-importer-impl
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

