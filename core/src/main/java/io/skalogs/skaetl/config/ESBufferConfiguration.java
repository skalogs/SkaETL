package io.skalogs.skaetl.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "buffer-elasticsearch")
public class ESBufferConfiguration {
    public Integer maxElements;
    public Integer maxSizeInBytes;
    public Integer maxTime;
    public final TimeUnit maxTimeUnit = SECONDS;

}
