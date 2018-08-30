package io.skalogs.skaetl.rules.metrics.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.repository.AbstractKafkaRepository;
import io.skalogs.skaetl.rules.metrics.domain.AggFunctionDescription;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Component;

@Component
public class AggFunctionDescriptionRepository extends AbstractKafkaRepository<AggFunctionDescription> {
    public AggFunctionDescriptionRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("agg-function-description",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(AggFunctionDescription.class)),
                aggFunctionDescription -> aggFunctionDescription.getName(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
