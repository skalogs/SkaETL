package io.skalogs.skaetl.service.processor;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
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
public class ProcessorBeanFactory {
    private final ESErrorRetryWriter esErrorRetryWriter;
    private final RestHighLevelClient client;
    private final ESConfiguration esConfiguration;
    private final ESBufferConfiguration esBufferConfiguration;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ValidateDataToElasticSearchProcessor validateDataToElasticSearchProcessor() {
        return new ValidateDataToElasticSearchProcessor(esErrorRetryWriter,client, esBufferConfiguration, esConfiguration);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JsonNodeToElasticSearchProcessor jsonNodeToElasticSearchProcessor(RetentionLevel retentionLevel, IndexShape indexShape) {
        return new JsonNodeToElasticSearchProcessor(esErrorRetryWriter, client, esBufferConfiguration, esConfiguration, retentionLevel, indexShape);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public LoggingProcessor loggingProcessor() {
        return new LoggingProcessor();
    }
}
