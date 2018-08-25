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
import io.skalogs.skaetl.domain.ProcessConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ProcessServiceHTTP {

    private final GeneratorConfiguration generatorConfiguration;

    public ProcessServiceHTTP(GeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public ProcessConsumer findProcess(String idProcess) {
        log.info("Call findProcess");
        RestTemplate restTemplate = new RestTemplate();
        ProcessConsumer obj = new ProcessConsumer();
        try {
            obj = restTemplate.getForObject(generatorConfiguration.getBackend() + "/process/findProcess?idProcess=" + idProcess, ProcessConsumer.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
        log.info("Result Call findProcess {} ", obj);
        return obj;
    }

    public void saveOrUpdate(ProcessConsumer processConsumer) {
        log.info("Call saveOrUpdate {}", processConsumer);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProcessConsumer> request = new HttpEntity<>(processConsumer);
        try {
            restTemplate.postForObject(generatorConfiguration.getBackend() + "/process/save", request, String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }

    }

    public void activateProcess(ProcessConsumer processConsumer) {
        log.info("Call activateProcess {}", processConsumer);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(generatorConfiguration.getBackend() + "/process/activate?idProcess=" + processConsumer.getIdProcess(), String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

}
