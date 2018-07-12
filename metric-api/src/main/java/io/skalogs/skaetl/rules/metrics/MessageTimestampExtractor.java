package io.skalogs.skaetl.rules.metrics;

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
