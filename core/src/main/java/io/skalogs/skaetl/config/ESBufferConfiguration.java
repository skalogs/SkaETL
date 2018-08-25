package io.skalogs.skaetl.config;

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
