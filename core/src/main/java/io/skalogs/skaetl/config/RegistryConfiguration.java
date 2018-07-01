package io.skalogs.skaetl.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegistryConfiguration {
    @Builder.Default
    private Boolean active = true;
}
