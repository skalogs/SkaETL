package io.skalogs.skaetl.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.ValidatorDescription;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Component;

@Component
public class ValidatorDescriptionRepository extends AbstractKafkaRepository<ValidatorDescription> {
    public ValidatorDescriptionRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("validator-description",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(ValidatorDescription.class)),
                validatorDescription -> validatorDescription.getName(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
