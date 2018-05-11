package io.skalogs.skaetl.rules.metrics;

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
