package io.skalogs.skaetl.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Streams;
import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.serdes.JsonNodeSerialializer;
import io.skalogs.skaetl.utils.JSONUtils;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.Getter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractKafkaRepository<V> {

    private final Producer<String, JsonNode> producer;
    private final ReadOnlyKeyValueStore<String, V> keyValueStore;
    private final Function<V, String> keyFunction;
    @Getter
    private final String repositoryName;

    public AbstractKafkaRepository(String name, Serde<V> valueSerde, Function<V,String> keyFunction, KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        this.repositoryName = name + "-db";
        this.keyFunction = keyFunction;
        this.producer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, JsonNodeSerialializer.class);
        kafkaAdminService.buildTopic(repositoryName);

        Properties props = KafkaUtils.createKStreamProperties(repositoryName + "-stream"+ UUID.randomUUID().toString(), kafkaConfiguration.getBootstrapServers());
        StreamsBuilder builder = new StreamsBuilder();

        final GlobalKTable<String, V> globalKTable = builder.globalTable(repositoryName, materialize(valueSerde));

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        producer.flush();
        keyValueStore = streams.store(getStoreName(), QueryableStoreTypes.keyValueStore());

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

    public void save(V objectToSave) {
        producer.send(new ProducerRecord<>(repositoryName, keyFunction.apply(objectToSave), JSONUtils.getInstance().toJsonNode(objectToSave)));
        producer.flush();
    }

    public List<V> findAll() {
        return Streams.stream(keyValueStore.all()).map(entry -> entry.value).filter(entry -> entry != null) .collect(Collectors.toList());
    }

    public V findByKey(String key) {
        return keyValueStore.get(key);
    }

    public void deleteByKey(String key) {

        producer.send(new ProducerRecord<>(repositoryName, key, null));
        producer.flush();
    }

    public void deleteAll(){
        keyValueStore.all().forEachRemaining(stringVKeyValue -> deleteByKey(stringVKeyValue.key));
    }

    private Materialized<String, V, KeyValueStore<Bytes, byte[]>> materialize(Serde<V> valueSerde) {
        return Materialized.<String, V, KeyValueStore<Bytes, byte[]>>as(getStoreName()).withKeySerde(Serdes.String()).withValueSerde(valueSerde);
    }

    private String getStoreName() {
        return repositoryName + "-local";
    }


}
