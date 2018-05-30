package io.skalogs.skaetl.domain;

import com.google.common.base.Stopwatch;
import io.skalogs.skaetl.config.ESBufferConfiguration;
import io.skalogs.skaetl.config.ESConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.MDC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Stopwatch.createUnstarted;
import static com.google.common.hash.Hashing.murmur3_128;
import static java.nio.charset.Charset.defaultCharset;

@Slf4j
public class ESBuffer {
    private final RestHighLevelClient elasticsearchClient;
    private final ESBufferConfiguration esBufferConfiguration;
    private final ESConfiguration esConfiguration;
    private BulkRequest bulk;
    private long sizeInBytes;
    private Stopwatch stopwatch = createUnstarted();
    private final List<String> values = new ArrayList<>();


    public ESBuffer(RestHighLevelClient elasticsearchClient, ESBufferConfiguration esBufferConfiguration, ESConfiguration esConfiguration) {
        this.elasticsearchClient = elasticsearchClient;
        this.esBufferConfiguration = esBufferConfiguration;
        this.esConfiguration = esConfiguration;
        reset();
    }

    public void add(Date timestamp, String project, String type, RetentionLevel retentionLevel, String value, String id) {
        sizeInBytes += value.length();

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String index = esConfiguration.getCustomIndexPrefix() + "-" + project + "-" + type + "-" + String.format("%04d", retentionLevel.nbDays) + "-" + simpleDateFormat.format(timestamp);
        values.add(value);
        log.debug("value add to bulk queue{}", value);
        String elasticSearchId = StringUtils.isNotBlank(id) ? generateId(id) : generateId(value);
        bulk.add(
                new IndexRequest(index.toLowerCase())
                        .type(project + "-" + type)
                        .id(elasticSearchId)
                        .source(value, XContentType.JSON));
    }

    private String generateId(String value) {
        return murmur3_128()
                .newHasher()
                .putString(value, defaultCharset())
                .hash()
                .toString();
    }

    public boolean needFlush() {

        return maxItemsReached() || maxBytesReached() || maxTimeReached();
    }

    public BulkResponse flush() throws IOException {
        MDC.put("flush_es_long", String.valueOf(values.size()));
        MDC.remove("flush_es_long");
        if (values.size() > 0) {
            return elasticsearchClient.bulk(bulk);
        }
        return null;
    }

    private boolean maxTimeReached() {
        return stopwatch.elapsed(esBufferConfiguration.maxTimeUnit) > esBufferConfiguration.maxTime;
    }

    private boolean maxBytesReached() {
        return sizeInBytes > esBufferConfiguration.maxSizeInBytes;
    }

    private boolean maxItemsReached() {
        return bulk.numberOfActions() >= esBufferConfiguration.maxElements;
    }

    public void reset() {
        this.bulk = new BulkRequest();
        this.sizeInBytes = 0;
        this.stopwatch.reset();
        this.stopwatch.start();
        this.values.clear();
    }

    public List<String> values() {
        return this.values;
    }

    public String getItem(int id) {
        return values.get(id);
    }
}
