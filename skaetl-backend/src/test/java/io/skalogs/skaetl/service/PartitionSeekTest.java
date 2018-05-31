package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class PartitionSeekTest {

    private final KafkaUtils kafkaUtils = new KafkaUtils(new KafkaConfiguration());


    @Test
    @Ignore
    public void toto() {

        Properties props = new Properties();
        KafkaConsumer kafkaConsumer = kafkaUtils.kafkaConsumerString("earliest", "localhost:9092", "simulate-json" + "", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer.subscribe(Arrays.asList("demo-cart"));
        kafkaConsumer.poll(1);
        log.error("begin");
        Set<TopicPartition> assignment = kafkaConsumer.assignment();
        Map<TopicPartition,Long> offsetsPerPartition = kafkaConsumer.endOffsets(assignment);
        offsetsPerPartition.entrySet().stream().forEach(e-> resetOffset(kafkaConsumer,e.getKey(),e.getValue()));
        ConsumerRecords<String, String> records = kafkaConsumer.poll(10);
        for (ConsumerRecord<String, String> record : records) {
            log.error(record.value());
        }
        kafkaConsumer.close();
    }

    private void resetOffset(KafkaConsumer kafkaConsumer, TopicPartition topicPartition, Long currentPosition) {
        Long newPosition = Math.max(currentPosition - 3, 0);
        log.error("Reseting partition position on {} partition {} to {}", topicPartition.topic(), topicPartition.partition(), newPosition);
        kafkaConsumer.seek(topicPartition, newPosition);
    }


}
