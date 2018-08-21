package io.skalogs.skaetl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "process")
public class ProcessConfiguration {
    private String urlRegistry;
    private Integer frequency;
    private Integer maxProcessConsumer;
    private String ipClient;
    private String portClient;
    private boolean active = true;
}
