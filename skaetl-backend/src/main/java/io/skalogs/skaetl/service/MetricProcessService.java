package io.skalogs.skaetl.service;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.domain.*;
import kafka.common.UnknownTopicOrPartitionException;
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
}
