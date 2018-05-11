package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.ESConfiguration;
import io.skalogs.skaetl.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class ReferentialService {

    private final RegistryService registryService;
    private final RestHighLevelClient restHighLevelClient;
    private final ESConfiguration esConfiguration;

    public ReferentialService(RegistryService registryService,RestHighLevelClient restHighLevelClient, ESConfiguration esConfiguration) {
        this.registryService = registryService;
        this.restHighLevelClient = restHighLevelClient;
        this.esConfiguration = esConfiguration;
    }

    public void activateProcess(ProcessReferential processReferential) throws Exception {
        registryService.activate(processReferential);
    }

    public void deactivateProcess(ProcessReferential processReferential) throws Exception {
        registryService.deactivate(processReferential);
    }

    public List<ConsumerState> findAll() {
        return registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream()
                .filter(consumerState -> consumerState.getStatusProcess() != StatusProcess.CREATION)
                .collect(toList());
    }

    public ProcessDefinition findReferential(String idReferential) {
        return registryService.findById(idReferential);
    }

    public void deleteReferential(String idReferential) {
        registryService.remove(registryService.findById(idReferential));
    }

    public ProcessReferential init() {
        String idProcess = UUID.randomUUID().toString();
        ProcessReferential processReferential = ProcessReferential.builder()
                .idProcess(idProcess)
                .build();
        return processReferential;
    }

    public void updateReferential(ProcessReferential processReferential) {
        registryService.createOrUpdateProcessDefinition(processReferential, WorkerType.REFERENTIAL_PROCESS, StatusProcess.INIT);
    }
}
