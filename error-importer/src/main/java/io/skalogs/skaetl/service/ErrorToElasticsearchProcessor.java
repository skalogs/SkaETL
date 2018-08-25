package io.skalogs.skaetl.service;

/*-
 * #%L
 * error-importer
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
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.ErrorData;
import io.skalogs.skaetl.domain.IndexShape;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.service.processor.AbstractElasticsearchProcessor;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.MDC;

import java.text.ParseException;

@Slf4j
public class ErrorToElasticsearchProcessor extends AbstractElasticsearchProcessor<String, ErrorData> {

    private static final String NO_PROJECT = "no-project";
    private static final String ERRORS = "errors";
    private final ISO8601DateFormat df = new ISO8601DateFormat();

    public ErrorToElasticsearchProcessor(ESErrorRetryWriter esErrorRetryWriter, RestHighLevelClient elasticsearchClient, ESBufferConfiguration esBufferConfiguration, ESConfiguration esConfiguration) {
        super(esErrorRetryWriter, elasticsearchClient, esBufferConfiguration, esConfiguration);
    }

    @Override
    public void process(String key, ErrorData errorData) {
        try {
            String valueAsString = JSONUtils.getInstance().asJsonString(errorData);
            processToElasticsearch(df.parse(errorData.timestamp), NO_PROJECT, ERRORS, RetentionLevel.week, IndexShape.daily, valueAsString, valueAsString);
        } catch (JsonProcessingException e) {
            log.error("Couldn't transform value " + errorData, e);
        } catch (ParseException e) {
            log.error("Couldn't parse date " + errorData, e);
        }
    }


    @Override
    protected void parseResultErrors(BulkRequest request, BulkResponse bulkItemResponses) {
        for (BulkItemResponse bir : bulkItemResponses) {
            MDC.put("item_error", bir.getFailureMessage());
            log.info("EsError" + bir.getFailureMessage());
            MDC.remove("item_error");
            //TODO ...
        }
    }
}
