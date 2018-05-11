package io.skalogs.skaetl.service.referential;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.MetadataItem;
import io.skalogs.skaetl.domain.Referential;
import io.skalogs.skaetl.serdes.GenericSerdes;
import io.skalogs.skaetl.serdes.JsonNodeSerialializer;
import io.skalogs.skaetl.service.processor.JsonNodeToElasticSearchProcessor;
import io.skalogs.skaetl.utils.JSONUtils;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ReferentialESService {

    private Map<String, Referential> referentialMap = new HashMap<>();
    private final Producer<String, JsonNode> referentialProducer;
    private final JsonNodeToElasticSearchProcessor elasticSearchProcessor;
    private final KafkaAdminService kafkaAdminService;
    private final String bootstrapServer;
    private final String TOPIC_REFERENTIAL_ES = "topicReferentialEs";

    public ReferentialESService(KafkaConfiguration kafkaConfiguration, JsonNodeToElasticSearchProcessor elasticSearchProcessor, KafkaAdminService kafkaAdminService) {
        this.referentialProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, JsonNodeSerialializer.class);
        this.elasticSearchProcessor = elasticSearchProcessor;
        this.bootstrapServer = kafkaConfiguration.getBootstrapServers();
        this.kafkaAdminService = kafkaAdminService;
    }

    @PostConstruct
    private void createStreamEs() {
        //Init topic
        kafkaAdminService.buildTopic(TOPIC_REFERENTIAL_ES);
        //Init Stream
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, JsonNode> streamToES = builder.stream(TOPIC_REFERENTIAL_ES, Consumed.with(Serdes.String(), GenericSerdes.jsonNodeSerde()));
        streamToES.process(() -> elasticSearchProcessor);
        KafkaStreams streams = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties("REFERENTIAL#ELASTICSEARCH", bootstrapServer));
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }

    @Scheduled(initialDelay = 1 * 60 * 1000, fixedRate = 5 * 60 * 1000)
    public void persist() {
        synchronized (this) {
            log.info("Persist Referential size {}", referentialMap.values().size());
            referentialMap.values().stream()
                    .forEach(referential -> referentialToKafka(referential));
        }
    }

    public void save(List<Referential> referentialList) {
        referentialList.stream().forEach(item -> compute(item));
    }

    public void forceFlush() {
        log.info(" Force Flush ");
        persist();
    }

    public void compute(Referential newReferential) {
        Referential ref = referentialMap.get(newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue());
        if (ref == null) {
            referentialMap.put(newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue(), newReferential);
        } else {
            referentialMap.put(newReferential.getIdProcessReferential() + "#" + newReferential.getKey() + "#" + newReferential.getValue(), mergeMetadata(ref
                    .withValue(newReferential.getValue())
                    .withTimestamp(newReferential.getTimestamp()), newReferential.getMetadataItemSet()));
        }
    }

    private Referential mergeMetadata(Referential referential, Set<MetadataItem> newMetadataItemSet) {
        newMetadataItemSet.stream().forEach(itemNew -> updateRefMetadata(referential, itemNew));
        return referential;
    }

    private void updateRefMetadata(Referential referential, MetadataItem itemNew) {
        Boolean noTreat = true;
        for (MetadataItem itemRef : referential.getMetadataItemSet()) {
            if (itemRef.getKey().equals(itemNew.getKey())) {
                itemRef.setValue(itemNew.getValue());
                noTreat = false;
            }
        }
        if (noTreat) {
            referential.getMetadataItemSet().add(itemNew);
        }
    }

    private void referentialToKafka(Referential referential) {
        referentialProducer.send(new ProducerRecord<>(TOPIC_REFERENTIAL_ES, JSONUtils.getInstance().toJsonNode(referential)));
    }


}
