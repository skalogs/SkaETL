package io.skalogs.skaetl.service.processor;

/*-
 * #%L
 * referential-importer-impl
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
public class ReferentialElasticsearchProcessor extends AbstractElasticsearchProcessor<String, JsonNode> {

    private final ISO8601DateFormat df = new ISO8601DateFormat();
    private final RetentionLevel retentionLevel;

    public ReferentialElasticsearchProcessor(ESErrorRetryWriter esErrorRetryWriter, RestHighLevelClient elasticsearchClient, ESBufferConfiguration esBufferConfiguration, ESConfiguration esConfiguration, RetentionLevel retentionLevel) {
        super(esErrorRetryWriter, elasticsearchClient, esBufferConfiguration, esConfiguration);
        this.retentionLevel = retentionLevel;
    }

    @Override
    public void process(String key, JsonNode referential) {
        try {
            String valueAsString = referential.toString();
            String timestamp = referential.path("timestamp").asText();
            String id = referential.path("id").asText() + "-" + referential.path("key").asText() + "-" + referential.path("value").asText() + "-" + referential.path("project").asText() + "-" + referential.path("type").asText();
            processToElasticsearch(df.parse(timestamp), referential.path("project").asText(), referential.path("type").asText(), retentionLevel, IndexShape.daily, valueAsString, id);
        } catch (ParseException e) {
            log.error("Couldn't extract timestamp " + referential.toString(), e);
        }
    }
}
