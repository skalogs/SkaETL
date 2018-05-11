package io.skalogs.skaetl.config;

import io.skalogs.skaetl.utils.AutoOffsetReset;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfiguration {
    private String topic;
    private String errorTopic = "es-error";
    private String retryTopic = "es-retry";
    private String groupId;
    private String bootstrapServers;
    private Integer pollingTime;
    private String pollRecord;
    private AutoOffsetReset autoOffsetReset = AutoOffsetReset.earliest;
}
