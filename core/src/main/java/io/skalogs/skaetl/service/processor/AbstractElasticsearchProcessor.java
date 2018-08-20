package io.skalogs.skaetl.service.processor;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.ESErrorRetryWriter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.hash.Hashing.murmur3_128;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.lang3.StringUtils.contains;

@Slf4j
@Getter
public abstract class AbstractElasticsearchProcessor<K, V> extends AbstractOutputProcessor<K, V> {

    private final ESErrorRetryWriter esErrorRetryWriter;
    private final ESConfiguration esConfiguration;
    private final BulkProcessor bulkProcessor;

    protected AbstractElasticsearchProcessor(ESErrorRetryWriter esErrorRetryWriter, RestHighLevelClient elasticsearchClient, ESBufferConfiguration esBufferConfiguration, ESConfiguration esConfiguration) {
        this.esErrorRetryWriter = esErrorRetryWriter;
        this.bulkProcessor = BulkProcessor.builder((request, bulkListener) -> elasticsearchClient.bulkAsync(request, bulkListener),
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                        log.info("{} flushing {} ...", getApplicationId(), request.numberOfActions());
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                        log.info("{} injected {} in {}", getApplicationId(), response.getItems().length, response.getTook().toString());

                        if (response.hasFailures()) {
                            //parse result for check if error or not
                            log.info("{} failures {}  {} ", getApplicationId(), response.hasFailures(), response.buildFailureMessage());
                            parseResultErrors(request, response);
                        }
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        log.error(getApplicationId() + " got technical error",  failure);
                        parseErrorsTechnical(request);
                    }
                })
                .setBulkActions(esBufferConfiguration.getMaxElements())
                .setBulkSize(new ByteSizeValue(esBufferConfiguration.getMaxSize(), esBufferConfiguration.getByteSizeUnit()))
                .setFlushInterval(TimeValue.timeValueMillis(esBufferConfiguration.getMaxTimeUnit().toMillis(esBufferConfiguration.getMaxTime())))
                .setConcurrentRequests(1)
                .setBackoffPolicy(esBufferConfiguration.toBackOffPolicy())
                .build();
        this.esConfiguration = esConfiguration;
    }

    protected void processToElasticsearch(Date date, String project, String type, RetentionLevel retentionLevel, IndexShape indexShape, String valueAsString) {
        processToElasticsearch(date, project, type, retentionLevel, indexShape, valueAsString, null);

    }

    protected void processToElasticsearch(Date date, String project, String type, RetentionLevel retentionLevel, IndexShape indexShape, String valueAsString, String id) {
        Metrics.counter("skaetl_nb_write_es_common",
                Lists.newArrayList(
                        Tag.of("processConsumerName", getApplicationId()),
                        Tag.of("project", project),
                        Tag.of("type", type)
                )
        ).increment();
        String pattern = indexShapePattern(indexShape, retentionLevel);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String index = esConfiguration.getCustomIndexPrefix() + "-" + project + "-" + type + "-" + String.format("%04d", retentionLevel.nbDays) + "-" + simpleDateFormat.format(date);
        String elasticSearchId = StringUtils.isNotBlank(id) ? generateId(id) : generateId(valueAsString);
        bulkProcessor.add(
                new IndexRequest(index.toLowerCase(), project + "-" + type, elasticSearchId)
                        .source(valueAsString, XContentType.JSON));
    }

    private String indexShapePattern(IndexShape indexShape, RetentionLevel retentionLevel) {
        String dailyPattern = "yyyy-MM-dd";
        //backward compatibility
        if (indexShape == null) {
            return dailyPattern;
        }
        //make no sense to build monthly indexes with those retention
        if (retentionLevel == RetentionLevel.day || retentionLevel == RetentionLevel.week) {
            return dailyPattern;
        }
        switch (indexShape) {
            case monthly:
                return "yyyy-MM";
            case daily:

                return dailyPattern;
            default:
                throw new IllegalArgumentException("Index shape not supported : " + indexShape);
        }
    }


    private String generateId(String value) {
        return murmur3_128()
                .newHasher()
                .putString(value, defaultCharset())
                .hash()
                .toString();
    }

    private void parseErrorsTechnical(BulkRequest bulkRequest) {
        bulkRequest.requests().stream()
                .filter(request -> request.opType() == DocWriteRequest.OpType.INDEX)
                .map(this::toRawMessage)
                .forEach(itemRaw -> esErrorRetryWriter.sendToRetryTopic(getApplicationId(), itemRaw));

    }

    private String toRawMessage(DocWriteRequest docWriteRequest) {
        if (docWriteRequest.opType() == DocWriteRequest.OpType.INDEX) {
            IndexRequest indexRequest = (IndexRequest) docWriteRequest;
            return new String(indexRequest.source().toBytesRef().utf8ToString());
        }
        return null;
    }

    protected void parseResultErrors(BulkRequest request, BulkResponse bulkItemResponses) {
        for (BulkItemResponse bir : bulkItemResponses) {
            DocWriteRequest docWriteRequest = request.requests().get(bir.getItemId());
            if (bir.isFailed() && isRetryable(bir)) {
                routeToNextTopic(bir, toRawMessage(docWriteRequest), false);
            } else {
                routeToNextTopic(bir, toRawMessage(docWriteRequest), true);
            }
        }
    }

    private void routeToNextTopic(BulkItemResponse bulkItemResponse, String itemRaw, boolean isErrorTopic) {
        log.debug("target bir is failed {} msg fail {} itemRaw {}", bulkItemResponse.isFailed(), bulkItemResponse.getFailureMessage(), itemRaw);
        if (itemRaw == null) {
            produceErrorToKafka(ValidateData.builder()
                    .timestamp(new Date())
                    .type("ERROR_PARSING")
                    .message("Failure parsing after send for itemId" + bulkItemResponse.getItemId())
                    .statusCode(StatusCode.parsing_invalid_after_send)
                    .success(false)
                    .value("Failure parsing after send" + bulkItemResponse.getFailureMessage()).build());
        } else if (isErrorTopic) {
            produceErrorToKafka(bulkItemResponse.getFailureMessage(), itemRaw);
        } else {
            esErrorRetryWriter.sendToRetryTopic(getApplicationId(), itemRaw);
        }
    }

    private void produceErrorToKafka(String messageFailure, String value) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        ErrorData error = ErrorData.builder()
                .errorReason(StatusCode.error_after_send_es.name())
                .errorMessage(messageFailure)
                .message(value)
                .timestamp(df.format(new Date()))
                .build();
        esErrorRetryWriter.sendToErrorTopic(getApplicationId(), error);
    }


    private void produceErrorToKafka(ValidateData validateData) {
        esErrorRetryWriter.sendToErrorTopic(getApplicationId(), validateData);
    }

    public boolean isRetryable(BulkItemResponse bir) {
        return bir.getType().equals("elasticsearch_http_ko")
                || contains(bir.getFailureMessage(), "java.net");
    }

    public String getApplicationId() {
        return context().applicationId();
    }

    @Override
    public boolean support(TypeOutput typeOutput) {
        return typeOutput == TypeOutput.ELASTICSEARCH;
    }
}