package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import lombok.AllArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class ErrorProcessorFactory {

    private final ESErrorRetryWriter esErrorRetryWriter;
    private final RestHighLevelClient client;
    private final ESConfiguration esConfiguration;
    private final ESBufferConfiguration esBufferConfiguration;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ErrorToElasticsearchProcessor errorToElasticsearchProcessor() {
        return new ErrorToElasticsearchProcessor(esErrorRetryWriter,client, esBufferConfiguration, esConfiguration);
    }

}
