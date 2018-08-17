package io.skalogs.skaetl.rules.metrics.processor;

import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.ESBuffer;
import io.skalogs.skaetl.domain.IndexShape;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import lombok.AllArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class MetricsElasticsearchProcessorBeanFactory {

    private final ESErrorRetryWriter esErrorRetryWriter;
    private final RestHighLevelClient client;
    private final ESConfiguration esConfiguration;
    private final ESBufferConfiguration esBufferConfiguration;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MetricsElasticsearchProcessor metricsElasticsearchProcessor(RetentionLevel retentionLevel, IndexShape indexShape) {
        ESBuffer esBuffer = new ESBuffer(client, esBufferConfiguration, esConfiguration);
        return new MetricsElasticsearchProcessor(esBuffer, esErrorRetryWriter, retentionLevel, indexShape);
    }
}
