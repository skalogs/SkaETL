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

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.domain.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class MetricProcessService {

    private final RegistryService registryService;
    private final KafkaAdminService kafkaAdminService;

    public List<ConsumerState> findAll() {
        return registryService.findAll(WorkerType.METRIC_PROCESS);
    }

    public void activateProcess(ProcessMetric processMetric) {
        registryService.activate(processMetric);
    }

    public void deactivateProcess(ProcessMetric processMetric) {
        registryService.deactivate(processMetric);
    }

    public ProcessDefinition findById(String id) {
        return registryService.findById(id);
    }

    public void deleteProcess(ProcessMetric processMetric) {
        registryService.remove(processMetric);
        kafkaAdminService.deleteTopics(processMetric.getFromTopic());
        processMetric.getProcessOutputs()
                .stream()
                .filter(processOutput -> processOutput.getTypeOutput() == TypeOutput.KAFKA)
                .forEach(processOutput -> kafkaAdminService.deleteTopic(processOutput.getParameterOutput().getTopicOut()));

    }

    public ProcessMetric init() {
        return ProcessMetric.builder()
                .idProcess(UUID.randomUUID().toString())
                .build();
    }

    public void updateProcess(ProcessMetric processMetric) {
        registryService.createOrUpdateProcessDefinition(processMetric,WorkerType.METRIC_PROCESS,StatusProcess.INIT);
    }

    public void scaleup(String idProcess) {
        registryService.scaleup(findById(idProcess));
    }

    public void scaledown(String idProcess) {
        registryService.scaledown(findById(idProcess));
    }

    public ConsumerState findConsumerState(String idProcess) {
        return registryService.findConsumerStateById(idProcess);
    }
}
