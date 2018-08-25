package io.skalogs.skaetl.service;

/*-
 * #%L
 * skaetl-backend
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

import io.skalogs.skaetl.config.PrometheusConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class PromTest {
    @Test
    public void callProm() {
        PrometheusConfiguration prometheusConfiguration = new PrometheusConfiguration();
        prometheusConfiguration.setHost("35.205.180.150:9090");
        PromService promService = new PromService(prometheusConfiguration);

        promService.fetchDataCapture("skaetl_nb_read_kafka_count", "processConsumerName", "demo-toto", 5).stream()
                .forEach(item -> log.error("mouarf " + item.toString()));

        promService.fetchDataCapture("skaetl_nb_read_kafka_count", "processConsumerName", "cart", 5).stream()
                .forEach(item -> log.error("demo " + item.toString()));
    }

}
