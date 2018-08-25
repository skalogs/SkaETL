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

import com.fasterxml.jackson.core.JsonProcessingException;
import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.IndexShape;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

@Slf4j
public class ValidateDataToElasticSearchProcessor extends AbstractElasticsearchProcessor<String, ValidateData> {

    public ValidateDataToElasticSearchProcessor(ESErrorRetryWriter esErrorRetryWriter, RestHighLevelClient elasticsearchClient, ESBufferConfiguration esBufferConfiguration, ESConfiguration esConfiguration) {
        super(esErrorRetryWriter, elasticsearchClient, esBufferConfiguration, esConfiguration);
    }

    @Override
    public void process(String key, ValidateData validateData) {
        RetentionLevel retentionLevel = validateData.jsonValue.has("retention") ? RetentionLevel.valueOf(validateData.jsonValue.path("retention").asText()) : RetentionLevel.week;

        try {
            String valueAsString = JSONUtils.getInstance().asJsonString(validateData);
            processToElasticsearch(validateData.timestamp, validateData.project, validateData.type, retentionLevel, IndexShape.daily, valueAsString, valueAsString);
        } catch (JsonProcessingException e) {
            log.error("Couldn't transform value " + validateData, e);
        }
    }
}
