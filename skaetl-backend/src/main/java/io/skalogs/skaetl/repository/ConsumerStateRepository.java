package io.skalogs.skaetl.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.ConsumerState;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Component;

@Component
public class ConsumerStateRepository extends AbstractKafkaRepository<ConsumerState> {
    public ConsumerStateRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("consumer-state",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(ConsumerState.class)),
                consumerState -> consumerState.getId(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
