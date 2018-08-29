package io.skalogs.skaetl.rules.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.repository.AbstractKafkaRepository;
import io.skalogs.skaetl.rules.domain.FilterFunctionDescription;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Component;

@Component
public class FilterFunctionDescriptionRepository extends AbstractKafkaRepository<FilterFunctionDescription> {
    public FilterFunctionDescriptionRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("parser-description",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(FilterFunctionDescription.class)),
                filterFunctionDescription -> filterFunctionDescription.getName(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
