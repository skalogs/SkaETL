package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.skalogs.skaetl.domain.ESBuffer;
import io.skalogs.skaetl.domain.IndexShape;
import io.skalogs.skaetl.domain.RetentionLevel;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidateDataToElasticSearchProcessor extends AbstractElasticsearchProcessor<String, ValidateData> {

    public ValidateDataToElasticSearchProcessor(ESBuffer esBuffer, ESErrorRetryWriter esErrorRetryWriter) {
        super(esBuffer, esErrorRetryWriter);
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
