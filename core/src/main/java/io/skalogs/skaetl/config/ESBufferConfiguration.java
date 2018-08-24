package io.skalogs.skaetl.config;


import io.skalogs.skaetl.domain.ElasticsearchBackoffPolicy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "buffer-elasticsearch")
@ToString
public class ESBufferConfiguration {
    private Integer maxElements = 2000;
    private Integer maxSize = 1;
    private ByteSizeUnit byteSizeUnit = ByteSizeUnit.MB;
    private Integer maxTime = 1;
    private TimeUnit maxTimeUnit = SECONDS;
    private ElasticsearchBackoffPolicy backoffPolicy = ElasticsearchBackoffPolicy.NO_BACKOFF;
    private Integer delayBackoff = 1;
    private TimeUnit delayBackoffTimeUnit = SECONDS;
    private Integer backoffMaxRetry = 3;
    private Integer concurrentRequests = 1;


    public BackoffPolicy toBackOffPolicy() {
        switch (backoffPolicy) {
            case NO_BACKOFF:
                return BackoffPolicy.noBackoff();
            case CONSTANT_BACKOFF:
                return BackoffPolicy.constantBackoff(TimeValue.timeValueMillis(delayBackoffTimeUnit.toMillis(delayBackoff)), backoffMaxRetry);
            case EXPONENTIAL_BACKOFF:
                return BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(delayBackoffTimeUnit.toMillis(delayBackoff)), backoffMaxRetry);
            default:
                throw new IllegalArgumentException(backoffPolicy + " not supported");
        }
    }

}
