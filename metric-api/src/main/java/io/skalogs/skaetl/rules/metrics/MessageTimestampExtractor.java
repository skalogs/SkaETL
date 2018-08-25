package io.skalogs.skaetl.rules.metrics;

/*-
 * #%L
 * metric-api
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
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.text.ParseException;

@Slf4j
public class MessageTimestampExtractor implements TimestampExtractor {

    private final ISO8601DateFormat df = new ISO8601DateFormat();

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        long timestamp = -1;
        final JsonNode jsonNode = (JsonNode) record.value();
        if (jsonNode.has("timestamp")) {
            try {
                timestamp = df.parse(jsonNode.path("timestamp").asText()).toInstant().toEpochMilli();
            } catch (ParseException e) {
                log.error("can't parse timestamp from " + jsonNode, e);
            }
        }
        if (false) {
            if (timestamp < 0) {
                // Invalid timestamp!  Attempt to estimate a new timestamp,
                // otherwise fall back to wall-clock time (processing-time).
                if (previousTimestamp >= 0) {
                    return previousTimestamp;
                } else {
                    return System.currentTimeMillis();
                }
            }
        }
        return timestamp;
    }
}
