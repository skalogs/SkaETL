package io.skalogs.skaetl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@Wither
public class ConsumerState {
    private final String id;
    private final Integer nbInstance;
    private final WorkerType workerType;
    private final Set<String> registryWorkers;
    private final StatusProcess statusProcess;
    private final ProcessDefinition processDefinition;

    public ConsumerState(ProcessDefinition processDefinition, WorkerType workerType, StatusProcess statusProcess) {
        this.id = processDefinition.getIdProcess();
        this.processDefinition = processDefinition;
        this.statusProcess = statusProcess;
        this.workerType = workerType;
        this.nbInstance = 1;
        this.registryWorkers = new HashSet<>();
    }
}
