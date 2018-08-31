package io.skalogs.skaetl.serdes;

/*-
 * #%L
 * core
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

import io.skalogs.skaetl.domain.GrokData;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class GrokDataDeserializer implements Deserializer<GrokData> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public GrokData deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return new GrokData();
        }
        GrokData grokData = null;
        try {
            grokData = JSONUtils.getInstance().parse(bytes, GrokData.class);
        } catch (Exception e) {
            log.error("GrokDataDeserializer message {}", e);
        }
        return grokData;
    }

    public void close() {

    }
}
