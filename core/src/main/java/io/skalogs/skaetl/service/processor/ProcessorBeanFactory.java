package io.skalogs.skaetl.service.processor;

import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.ESBuffer;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import lombok.AllArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class ProcessorBeanFactory {
    private final ESErrorRetryWriter esErrorRetryWriter;
    private final RestHighLevelClient client;
    private final ESConfiguration esConfiguration;
    private final ESBufferConfiguration esBufferConfiguration;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ValidateDataToElasticSearchProcessor validateDataToElasticSearchProcessor() {
        ESBuffer esBuffer = new ESBuffer(client, esBufferConfiguration, esConfiguration);
        return new ValidateDataToElasticSearchProcessor(esBuffer, esErrorRetryWriter);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JsonNodeToElasticSearchProcessor jsonNodeToElasticSearchProcessor() {
        ESBuffer esBuffer = new ESBuffer(client, esBufferConfiguration, esConfiguration);
        return new JsonNodeToElasticSearchProcessor(esBuffer, esErrorRetryWriter);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ReferentialElasticsearchProcessor referentialElasticsearchProcessor() {
        ESBuffer esBuffer = new ESBuffer(client, esBufferConfiguration, esConfiguration);
        return new ReferentialElasticsearchProcessor(esBuffer, esErrorRetryWriter);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public LoggingProcessor loggingProcessor() {
        return new LoggingProcessor();
    }
}
