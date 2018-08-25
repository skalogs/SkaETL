package io.skalogs.skaetl.service;

/*-
 * #%L
 * generator
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

import io.skalogs.skaetl.config.GeneratorConfiguration;
import io.skalogs.skaetl.domain.ProcessMetric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MetricServiceHTTP {
    private final GeneratorConfiguration generatorConfiguration;

    public MetricServiceHTTP(GeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public ProcessMetric findById(String idProcess) {
        log.info("Call findById");
        RestTemplate restTemplate = new RestTemplate();
        ProcessMetric obj = new ProcessMetric();
        try {
            obj = restTemplate.getForObject(generatorConfiguration.getBackend() + "/metric/findById?idProcess=" + idProcess, ProcessMetric.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
        log.info("Result Call findById {} ", obj);
        return obj;
    }

    public void updateProcess(ProcessMetric processMetric) {
        log.info("Call updateProcess {}", processMetric);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProcessMetric> request = new HttpEntity<>(processMetric);
        try {
            restTemplate.postForObject(generatorConfiguration.getBackend() + "/metric/update", request, String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

    public void activateProcess(ProcessMetric processMetric) {
        log.info("Call activateProcess {}", processMetric);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(generatorConfiguration.getBackend() + "/metric/activate?idProcess=" + processMetric.getIdProcess(), String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

}
