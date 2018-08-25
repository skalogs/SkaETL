package io.skalogs.skaetl.service;

/*-
 * #%L
 * skaetl-backend
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

import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class KafkaService {

    private KafkaConsumer<String, String> kafkaConsumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public KafkaService(KafkaUtils kafkaUtils) {
        kafkaConsumer = kafkaUtils.kafkaConsumer();
    }

    public ConsumerRecords<String, String> extractDataFromKafka(String topic, long duration, TimeUnit timeUnit) {
        long pollingTime = timeUnit.toMillis(duration);
        log.info("Capture data during {} ms on topic {}", pollingTime, topic);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        try {
            return kafkaConsumer.poll(pollingTime);
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } catch (RuntimeException re) {
            log.error("RuntimeException {}", re);
        } finally {
            if (closed.get()) {
                kafkaConsumer.close();
            }
            return null;
        }
    }

    public List<String> catpureData(String topic, long duration, TimeUnit timeUnit) {
        List<String> result = new ArrayList<>();
        ConsumerRecords<String, String> records = extractDataFromKafka(topic, duration, timeUnit);
        for (ConsumerRecord<String, String> record : records) {
            result.add(record.value());
        }
        return result;
    }
}
