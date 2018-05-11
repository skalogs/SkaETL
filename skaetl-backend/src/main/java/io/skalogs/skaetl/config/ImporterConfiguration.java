package io.skalogs.skaetl.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "importer")
@Slf4j
public class ImporterConfiguration {
    private String fullUrlImporter;
    private String fullUrlSimulate;

}
