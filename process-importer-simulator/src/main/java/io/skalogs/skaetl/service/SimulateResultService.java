package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.SimulateData;
import io.skalogs.skaetl.utils.KafkaUtils;
import io.skalogs.skaetl.utils.Rebalancer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

import java.util.*;

import static io.skalogs.skaetl.domain.ProcessConstants.SIMULATE_OUTPUT;


@Component
@AllArgsConstructor
@Slf4j
public class SimulateResultService {

    private final KafkaUtils kafkaUtils;

    public List<SimulateData> readOutPut(String bootStrapServers, String maxRecords, String windowTime) {
        KafkaConsumer kafkaConsumer = kafkaUtils.kafkaConsumer("latest", bootStrapServers, "simulate");
        log.info("Subscribe Topic for {}", SIMULATE_OUTPUT);
        kafkaConsumer.subscribe(Arrays.asList(SIMULATE_OUTPUT), new Rebalancer());
        List<SimulateData> res = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            while (checkWindow(start, Long.valueOf(windowTime), res.size(), Long.valueOf(maxRecords))) {
                ConsumerRecords<String, SimulateData> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, SimulateData> record : records) {
                    res.add(record.value());
                }
                log.info("Number item for read OutPut {}", res.size());
                kafkaConsumer.commitSync();
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            throw e;
        } catch (RuntimeException re) {
            log.error("RuntimeException {}", re);
        } finally {
            kafkaConsumer.close();
        }
        return res;
    }

    private Boolean checkWindow(long start, Long windowTime, long sizeList, long maxSizeItems) {
        long current = System.currentTimeMillis();
        if (current >= (start + windowTime.longValue())) {
            return false;
        }
        if (sizeList >= maxSizeItems) {
            return false;
        }
        return true;
    }

    public List<String> readKafkaRawData(String bootStrapServers, String topic, String maxRecords, String windowTime, String offReset, String deserializer) {
        KafkaConsumer kafkaConsumer;
        if (offReset.equalsIgnoreCase("last")) {
            kafkaConsumer = kafkaUtils.kafkaConsumerString("latest", bootStrapServers, "simulate-raw" + UUID.randomUUID().toString(), deserializer);
            kafkaConsumer.subscribe(Arrays.asList(topic), new Rebalancer());
            kafkaConsumer.poll(1);
            Set<TopicPartition> assignment = kafkaConsumer.assignment();
            Map<TopicPartition, Long> offsetsPerPartition = kafkaConsumer.endOffsets(assignment);
            offsetsPerPartition.entrySet().stream().forEach(e -> resetOffset(kafkaConsumer, e.getKey(), Math.max(e.getValue() - Long.valueOf(maxRecords), 0)));
        } else {
            kafkaConsumer = kafkaUtils.kafkaConsumerString(offReset, bootStrapServers, "simulate-raw" + UUID.randomUUID().toString(), deserializer);
            kafkaConsumer.subscribe(Arrays.asList(topic), new Rebalancer());
        }

        log.info("Subscribe Topic for {} with parameter offReset {} windowTime {} maxRecords {} ", topic, offReset, windowTime, maxRecords);

        List<String> res = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            while (checkWindow(start, Long.valueOf(windowTime), res.size(), Long.valueOf(maxRecords))) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    log.error(record.value());
                    res.add(record.value());
                }
                kafkaConsumer.commitSync();
            }
            log.info("Number item for read Raw Data {}", res.size());
        } catch (WakeupException e) {
            // Ignore exception if closing
            throw e;
        } catch (RuntimeException re) {
            log.error("RuntimeException {}", re);
        } finally {
            kafkaConsumer.close();
        }
        return res;
    }

    private void resetOffset(KafkaConsumer kafkaConsumer, TopicPartition topicPartition, long newPosition) {
        log.error("Reseting partition position on {} partition {} to {}", topicPartition.topic(), topicPartition.partition(), newPosition);
        kafkaConsumer.seek(topicPartition, newPosition);
    }

    public List<String> readKafkaString(String bootStrapServers, String topic, String maxRecords, String windowTime, String idProcess) {
        KafkaConsumer kafkaConsumer = kafkaUtils.kafkaConsumerString("latest", bootStrapServers, "simulate-string" + idProcess, "org.apache.kafka.common.serialization.StringDeserializer");
        log.info("Subscribe Topic for {}", topic);
        kafkaConsumer.subscribe(Arrays.asList(topic), new Rebalancer());
        List<String> res = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            while (checkWindow(start, Long.valueOf(windowTime), res.size(), Long.valueOf(maxRecords))) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    res.add(record.value());
                }
                log.info("Number item for read String data {}", res.size());
                kafkaConsumer.commitSync();
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            throw e;
        } catch (RuntimeException re) {
            log.error("RuntimeException {}", re);
        } finally {
            kafkaConsumer.close();
        }
        return res;
    }

    public List<JsonNode> readKafkaJson(String bootStrapServers, String topic, String maxRecords, String windowTime, String idProcess) {
        KafkaConsumer kafkaConsumer = kafkaUtils.kafkaConsumerJsonNode("latest", bootStrapServers, "simulate-json" + idProcess);
        log.info("Subscribe Topic for {}", topic);
        kafkaConsumer.subscribe(Arrays.asList(topic), new Rebalancer());
        List<JsonNode> res = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            while (checkWindow(start, Long.valueOf(windowTime), res.size(), Long.valueOf(maxRecords))) {
                ConsumerRecords<String, JsonNode> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, JsonNode> record : records) {
                    res.add(record.value());
                }
                log.info("Number item for read Json Data {}", res.size());
                kafkaConsumer.commitSync();
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            throw e;
        } catch (RuntimeException re) {
            log.error("RuntimeException {}", re);
        } finally {
            kafkaConsumer.close();
        }
        return res;
    }

}
