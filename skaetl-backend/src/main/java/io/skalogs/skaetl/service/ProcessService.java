package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class ProcessService {

    private final RegistryService registryService;

    public ProcessService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public List<ConsumerState> findAll() {
        return registryService.findAll(WorkerType.PROCESS_CONSUMER).stream()
                .filter(consumerState -> consumerState.getStatusProcess() != StatusProcess.CREATION)
                .collect(toList());
    }

    public void activateProcess(ProcessConsumer processConsumer) throws Exception {
        registryService.activate(processConsumer);
    }

    public void deactivateProcess(ProcessConsumer processConsumer) throws Exception {
        registryService.deactivate(processConsumer);
    }

    public ProcessConsumer initProcessConsumer() {
        String idProcess = UUID.randomUUID().toString();
        ProcessConsumer processConsumer = ProcessConsumer.builder()
                .idProcess(idProcess)
                .processInput(ProcessInput.builder().host("localhost").port("9092").build())
                .build();
        return processConsumer;
    }

    public void saveOrUpdate(ProcessConsumer processConsumer) {
        registryService.createOrUpdateProcessDefinition(processConsumer,WorkerType.PROCESS_CONSUMER,StatusProcess.INIT);
    }

    public void deleteProcess(String idProcess) {
        registryService.remove(findProcess(idProcess));
    }

    public ProcessConsumer findProcess(String idProcess) {
        return registryService.findById(idProcess) != null ? (ProcessConsumer) registryService.findById(idProcess) : null;
    }

    public ConsumerState findConsumerState(String idProcess) {
        return registryService.findConsumerStateById(idProcess);
    }
}
