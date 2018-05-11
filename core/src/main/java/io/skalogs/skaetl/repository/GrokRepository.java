package io.skalogs.skaetl.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrokRepository extends AbstractKafkaRepository<String> {

    private final Producer<String, String> producerGrok;

    public GrokRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("grok-referential",
                Serdes.String(),
                grokRawData -> grokRawData,
                kafkaAdminService,
                kafkaConfiguration);
        this.producerGrok = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, StringSerializer.class);
    }

    public void save(String key, String value) {
        producerGrok.send(new ProducerRecord<>(getRepositoryName(), key, value));
        producerGrok.flush();
    }

}
