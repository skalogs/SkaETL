package io.skalogs.skaetl.rules.metrics;

/*-
 * #%L
 * metric-importer
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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.kafka.KafkaUnit;
import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class MetricResultMessageExtractor implements KafkaUnit.MessageExtractor<Keys, MetricResult> {

    @Override
    public KafkaUnit.Message<Keys, MetricResult> extract(ConsumerRecord<byte[], byte[]> record) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Keys keys = objectMapper.readValue(record.key(), Keys.class);
            MetricResult value = objectMapper.readValue(record.value(), MetricResult.class);

            return new KafkaUnit.Message<>(keys, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
