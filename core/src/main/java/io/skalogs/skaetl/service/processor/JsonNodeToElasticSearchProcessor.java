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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.IndexShape;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

import java.text.ParseException;

@Slf4j
public class JsonNodeToElasticSearchProcessor extends AbstractElasticsearchProcessor<String, JsonNode> {
    private final ISO8601DateFormat df = new ISO8601DateFormat();
    private final RetentionLevel retentionLevel;
    private final IndexShape indexShape;

    public JsonNodeToElasticSearchProcessor(ESErrorRetryWriter esErrorRetryWriter, RestHighLevelClient elasticsearchClient, ESBufferConfiguration esBufferConfiguration, ESConfiguration esConfiguration, RetentionLevel retentionLevel, IndexShape indexShape) {
        super(esErrorRetryWriter, elasticsearchClient, esBufferConfiguration, esConfiguration);
        this.retentionLevel = retentionLevel;
        this.indexShape = indexShape;
    }

    @Override
    public void process(String key, JsonNode jsonNode) {
        RetentionLevel retentionLvl = jsonNode.has("retention") ? RetentionLevel.valueOf(jsonNode.path("retention").asText()) : retentionLevel;
        String valueAsString = jsonNode.toString();
        String timestamp = jsonNode.path("timestamp").asText();
        String elasticSearchId = jsonNode.has("id") ? jsonNode.get("id").asText(): null;
        try {
            processToElasticsearch(df.parse(timestamp), jsonNode.path("project").asText(), jsonNode.path("type").asText(), retentionLvl, indexShape, valueAsString, elasticSearchId);
        } catch (ParseException e) {
            log.error("Couldn't extract timestamp " + jsonNode.toString(), e);
        }
    }
}
