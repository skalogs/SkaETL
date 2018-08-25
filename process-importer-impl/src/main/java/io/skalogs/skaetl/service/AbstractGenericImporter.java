package io.skalogs.skaetl.service;

/*-
 * #%L
 * process-importer-impl
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

import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.config.RegistryConfiguration;
import io.skalogs.skaetl.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class AbstractGenericImporter {


    private static final int NUM_CONSUMERS = 10;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_CONSUMERS);
    private final Map<ProcessConsumer,AbstractStreamProcess> runningConsumers = new HashMap<>();
    private final GenericValidator genericValidator;
    private final GenericTransformator genericTransformator;
    private final GenericParser genericParser;
    private final GenericFilterService genericFilterService;
    private final ProcessConfiguration processConfiguration;
    private final ExternalHTTPService externalHTTPService;
    private final RegistryConfiguration registryConfiguration;


    public void sendToRegistry(String action) {
        if (registryConfiguration.getActive()) {
            RegistryWorker registry = null;
            try {
                registry = RegistryWorker.builder()
                        .workerType(WorkerType.PROCESS_CONSUMER)
                        .ip(InetAddress.getLocalHost().getHostName())
                        .name(InetAddress.getLocalHost().getHostName())
                        .port(getProcessConfiguration().getPortClient())
                        .statusConsumerList(statusExecutor())
                        .build();
                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<RegistryWorker> request = new HttpEntity<>(registry);
                String url = getProcessConfiguration().getUrlRegistry();
                String res = restTemplate.postForObject(url + "/process/registry/" + action, request, String.class);
                log.debug("sendToRegistry result {}", res);
            } catch (Exception e) {
                log.error("Exception on sendToRegistry", e);
            }
        }
    }

    public void disableAll() {
        //Revoke Cache
        getRunningConsumers().keySet()
                .stream()
                .forEach(processConsumer -> externalHTTPService.revokeCache(processConsumer));

        // Shutdown all kafkastreams
        getRunningConsumers().values()
                .forEach(processStream -> processStream.shutdownAllStreams());

        // Remove the process Consumer
        getRunningConsumers().clear();

        // Update the registry
        sendToRegistry("refresh");
    }

    public void disable(ProcessConsumer processConsumer) {
        externalHTTPService.revokeCache(processConsumer);

        if (getRunningConsumers().containsKey(processConsumer)) {
            // Shutdown all kafkastreams
            getRunningConsumers().get(processConsumer)
                    .shutdownAllStreams();

            // Remove the process Consumer
            getRunningConsumers().remove(processConsumer);
        }

        // Update the registry
        sendToRegistry("refresh");
    }

    public List<StatusConsumer> statusExecutor() {
        return runningConsumers
                .keySet()
                .stream()
                .map(processConsumer -> StatusConsumer.builder()
                        .statusProcess(StatusProcess.ENABLE)
                        .creation(processConsumer.getTimestamp())
                        .idProcessConsumer(processConsumer.getIdProcess())
                        .build())
                .collect(toList());
    }


}
