package io.skalogs.skaetl.repository;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.ParserDescription;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Component;

@Component
public class ParserDescriptionRepository extends AbstractKafkaRepository<ParserDescription> {
    public ParserDescriptionRepository(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        super("parser-description",
                Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(ParserDescription.class)),
                parserDescription -> parserDescription.getName(),
                kafkaAdminService,
                kafkaConfiguration);
    }
}
