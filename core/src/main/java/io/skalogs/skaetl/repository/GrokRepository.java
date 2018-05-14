package io.skalogs.skaetl.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.GrokData;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrokRepository extends AbstractKafkaRepository<GrokData> {

    public GrokRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("grok-referential",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(GrokData.class)),
                grokRawData -> grokRawData.getKey(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
