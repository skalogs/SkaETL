package io.skalogs.skaetl.utils;

import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.GrokData;
import io.skalogs.skaetl.serdes.GrokDataSerializer;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@AllArgsConstructor
public class KafkaUtils {

    private final KafkaConfiguration kafkaConfiguration;

    public KafkaConsumer<String, String> kafkaConsumer() {
        return kafkaConsumer(kafkaConfiguration.getAutoOffsetReset().toString(), kafkaConfiguration.getBootstrapServers(), kafkaConfiguration.getGroupId());
    }

    public KafkaConsumer<String, String> kafkaConsumer(String offReset, String bootStrapServers, String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offReset);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.skalogs.skaetl.serdes.SimulateDataDeserializer");
        return new KafkaConsumer<>(props);
    }

    public KafkaConsumer<String, String> kafkaConsumerString(String offReset, String bootStrapServers, String groupId, String deserializer) {
        Properties props = new Properties();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offReset);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "20000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1000");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,"300000");
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,"30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,"5048576");
        return new KafkaConsumer<>(props);
    }

    public KafkaConsumer<String, String> kafkaConsumerJsonNode(String offReset, String bootStrapServers, String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offReset);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.skalogs.skaetl.domain.JsonNodeDeserializer");
        return new KafkaConsumer<>(props);
    }


    public Producer<String, String> kafkaProducer(String bootstrapServer) {
        return kafkaProducer(bootstrapServer, StringSerializer.class, StringSerializer.class);
    }

    public Producer<String, GrokData> kafkaGrokProducer() {
        return kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, GrokDataSerializer.class);
    }

    public static <K, V> Producer<K, V> kafkaProducer(String bootstrapServer, Class<? extends Serializer<K>> keySerializer, Class<? extends Serializer<V>> valueSerializer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.getName());
        return new KafkaProducer<>(props);
    }

    public Producer<String, String> kafkaProducer() {
        return kafkaProducer(kafkaConfiguration.getBootstrapServers());
    }

    public static Properties createKStreamProperties(String nameProcess, String bootstrapServers) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "application-process" + nameProcess);
        props.put(StreamsConfig.CLIENT_ID_CONFIG, nameProcess);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return props;
    }
}
