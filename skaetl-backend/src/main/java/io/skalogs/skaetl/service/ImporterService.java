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

import io.skalogs.skaetl.config.ImporterConfiguration;
import io.skalogs.skaetl.domain.PayloadReadOutput;
import io.skalogs.skaetl.domain.PayloadTextForReadOutput;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.SimulateData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class ImporterService {

    private final ImporterConfiguration importerConfiguration;
    private final ProcessService processService;

    public ImporterService(ImporterConfiguration importerConfiguration, ProcessService processService) {
        this.importerConfiguration = importerConfiguration;
        this.processService = processService;
    }

    public void launchSimulate(ProcessConsumer processConsumer) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProcessConsumer> request = new HttpEntity<>(processConsumer);
        restTemplate.postForObject(importerConfiguration.getFullUrlSimulate() + "/manage/active", request, String.class);
    }


    public SimulateData captureFromText(PayloadTextForReadOutput payloadTextForReadOutput) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PayloadTextForReadOutput> request = new HttpEntity<>(payloadTextForReadOutput);
        SimulateData objStatus = new SimulateData();
        try {
            objStatus = restTemplate.postForObject(importerConfiguration.getFullUrlSimulate() + "/manage/readOutputFromText", request, SimulateData.class);
            return objStatus;
        } catch (Exception e) {
            log.error("status {}", e);
        }
        return objStatus;
    }

    public List<SimulateData> capture(PayloadReadOutput payloadReadOutput) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PayloadReadOutput> request = new HttpEntity<>(payloadReadOutput);
        try {
            SimulateData[] objStatus = restTemplate.postForObject(importerConfiguration.getFullUrlSimulate() + "/manage/readOutput", request, SimulateData[].class);
            return Arrays.asList(objStatus);
        } catch (Exception e) {
            log.error("status {}", e);
        }
        return new ArrayList<>();
    }

    public List<String> captureRawData(PayloadReadOutput payloadReadOutput) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PayloadReadOutput> request = new HttpEntity<>(payloadReadOutput);
        try {
            String[] objStatus = restTemplate.postForObject(importerConfiguration.getFullUrlSimulate() + "/manage/captureRawData", request, String[].class);
            return Arrays.asList(objStatus);
        } catch (Exception e) {
            log.error("status {}", e);
        }
        return new ArrayList<>();
    }



}
