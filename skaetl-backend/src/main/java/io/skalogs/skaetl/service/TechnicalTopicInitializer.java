package io.skalogs.skaetl.service;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class TechnicalTopicInitializer {

    public TechnicalTopicInitializer(KafkaAdminService kafkaAdminService, KafkaConfiguration kafkaConfiguration) {
        //Create default error and retry topic on startup
        kafkaAdminService.buildTopic(kafkaConfiguration.getErrorTopic());
        kafkaAdminService.buildTopic(kafkaConfiguration.getRetryTopic());
    }
}
