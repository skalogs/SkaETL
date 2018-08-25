package io.skalogs.skaetl.service.processor;

/*-
 * #%L
 * services
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
