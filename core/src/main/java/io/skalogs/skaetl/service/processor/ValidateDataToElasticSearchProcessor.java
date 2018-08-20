package io.skalogs.skaetl.service.processor;

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
