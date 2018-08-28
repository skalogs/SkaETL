package io.skalogs.skaetl.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.TransformatorDescription;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Component;

@Component
public class TransformatorDescriptionRepository extends AbstractKafkaRepository<TransformatorDescription> {
    public TransformatorDescriptionRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("validator-description",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(TransformatorDescription.class)),
                transformatorDescription -> transformatorDescription.getName(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
