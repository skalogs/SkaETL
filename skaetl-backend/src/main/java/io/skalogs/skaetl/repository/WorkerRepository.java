package io.skalogs.skaetl.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.RegistryWorker;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Component;

@Component
public class WorkerRepository extends AbstractKafkaRepository<RegistryWorker> {
    public WorkerRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("worker",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(RegistryWorker.class)),
                registryWorker -> registryWorker.getFQDN(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
