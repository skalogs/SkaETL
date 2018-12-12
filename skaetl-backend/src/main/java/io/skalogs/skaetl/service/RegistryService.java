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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.repository.ConsumerStateRepository;
import io.skalogs.skaetl.repository.WorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RegistryService {

    private final ProcessConfiguration processConfiguration;
    private final WorkerRepository workerRepository;
    private final ConsumerStateRepository consumerStateRepository;

    private final AtomicLong workerOK = Metrics.gauge("skaetl_nb_worker", Lists.newArrayList(Tag.of("status", StatusWorker.OK.name())), new AtomicLong(0));
    private final AtomicLong workerKO = Metrics.gauge("skaetl_nb_worker", Lists.newArrayList(Tag.of("status", StatusWorker.KO.name())), new AtomicLong(0));
    private final AtomicLong workerFULL = Metrics.gauge("skaetl_nb_worker", Lists.newArrayList(Tag.of("status", StatusWorker.FULL.name())), new AtomicLong(0));


    public RegistryService(ProcessConfiguration processConfiguration, WorkerRepository workerRepository, ConsumerStateRepository consumerStateRepository) {
        this.processConfiguration = processConfiguration;
        this.workerRepository = workerRepository;
        this.consumerStateRepository = consumerStateRepository;
    }

    public List<RegistryWorker> getAllStatus() {
        return workerRepository.findAll();
    }

    // WORKERS APIs

    public void addHost(RegistryWorker registryWorker) {
        log.info("Registering {} as {}", registryWorker.getName(), registryWorker.getWorkerType());
        workerRepository.save(RegistryWorker.builder()
                .name(registryWorker.getName())
                .port(registryWorker.getPort())
                .dateRefresh(new Date())
                .ip(registryWorker.getIp())
                .workerType(registryWorker.getWorkerType())
                .status(StatusWorker.OK)
                .statusConsumerList(registryWorker.getStatusConsumerList())
                .build());
        workerOK.incrementAndGet();
        flagAssignedTaskAsDegraded(registryWorker);
    }

    private void flagAssignedTaskAsDegraded(RegistryWorker registryWorker) {
        consumerStateRepository.findAll()
                .stream()
                .filter(e -> e.getRegistryWorkers().contains(registryWorker.getFQDN()))
                .forEach(consumerState -> flagAssignedTaskAsDegraded(consumerState, registryWorker));
    }

    private void flagAssignedTaskAsDegraded(ConsumerState consumerState, RegistryWorker registryWorker) {
        log.info("Marking {} as degraded", consumerState.getProcessDefinition());
        consumerState.getRegistryWorkers().remove(registryWorker.getFQDN());
        ConsumerState newState = consumerState.withStatusProcess(StatusProcess.DEGRADED);
        consumerStateRepository.save(newState);
    }

    public void refresh(RegistryWorker registryWorker) {
        RegistryWorker registry = workerRepository.findByKey(registryWorker.getFQDN());
        if (registry == null) {
            log.info("Refresh but not registry for item {}", registryWorker);
            addHost(registryWorker);
        } else {
            registry.setStatus(statusWorker(registry.getDateRefresh(), registryWorker));
            registry.setDateRefresh(new Date());
            registry.setPort(registryWorker.getPort());
            workerRepository.save(registry);
        }
    }

    // APIs that should be used by etl-backend

    public ConsumerState findConsumerStateById(String id) {
        return consumerStateRepository.findByKey(id);
    }

    public ProcessDefinition findById(String id) {
        ConsumerState processDefinition = consumerStateRepository.findByKey(id);
        if (processDefinition == null) {
            return null;
        }
        return processDefinition.getProcessDefinition();
    }

    public List<ConsumerState> findAll(WorkerType workerType) {
        return consumerStateRepository.findAll().stream()
                .filter(consumerState -> consumerState.getWorkerType() == workerType)
                .collect(Collectors.toList());
    }

    public List<ConsumerState> findAll() {
        return consumerStateRepository.findAll().stream()
                .collect(Collectors.toList());
    }

    public void activate(ProcessDefinition processDefinition) {
        ConsumerState consumerState = consumerStateRepository.findByKey(processDefinition.getIdProcess()).withProcessDefinition(processDefinition);
        consumerState = assignConsumerToWorkers(consumerState);
        triggerAction(consumerState, "activate", StatusProcess.ENABLE, StatusProcess.ERROR);

    }

    public void remove(ProcessDefinition processDefinition) {
        deactivate(processDefinition);
        consumerStateRepository.deleteByKey(processDefinition.getIdProcess());
    }

    public void deactivate(ProcessDefinition processDefinition) {
        ConsumerState fromDB = consumerStateRepository.findByKey(processDefinition.getIdProcess());
        if (fromDB != null) {
            ConsumerState consumerState = fromDB.withProcessDefinition(processDefinition);
            triggerAction(consumerState, "deactivate", StatusProcess.DISABLE, StatusProcess.DISABLE);
        }
    }

    public void createOrUpdateProcessDefinition(ProcessDefinition processDefinition, WorkerType workerType, StatusProcess statusProcess) {
        ConsumerState consumerState = consumerStateRepository.findByKey(processDefinition.getIdProcess());
        if (consumerState == null) {
            consumerState = new ConsumerState(processDefinition, workerType, statusProcess);
            consumerStateRepository.save(consumerState);
        } else {
            consumerStateRepository.save(consumerState.withProcessDefinition(processDefinition));
        }
    }

    public void scaleup(ProcessDefinition processDefinition) {
        ConsumerState consumerState = consumerStateRepository.findByKey(processDefinition.getIdProcess());
        ConsumerState newState = consumerState.withNbInstance(consumerState.getNbInstance() + 1);

        if (consumerState.getStatusProcess() == StatusProcess.ENABLE) {
            deactivate(processDefinition);
            consumerState = assignConsumerToWorkers(newState);
            triggerAction(consumerState, "activate", StatusProcess.ENABLE, StatusProcess.ERROR);
        }  else {
            consumerStateRepository.save(newState);
        }
    }

    public void scaledown(ProcessDefinition processDefinition) {
        ConsumerState consumerState = consumerStateRepository.findByKey(processDefinition.getIdProcess());
        int nbInstance = consumerState.getNbInstance() - 1;
        //can't get less than 1 instance
        if (nbInstance >= 1) {
            ConsumerState newState = consumerState.withNbInstance(nbInstance);

            if (consumerState.getStatusProcess() == StatusProcess.ENABLE && consumerState.getRegistryWorkers().size() > nbInstance) {
                String last = Iterables.getLast(consumerState.getRegistryWorkers());
                RegistryWorker worker = workerRepository.findByKey(last);
                log.info("triggering deactivate on worker {} from {}", last, processDefinition);
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    HttpEntity<ProcessDefinition> request = new HttpEntity<>(consumerState.getProcessDefinition());
                    restTemplate.postForObject(worker.getBaseUrl() + "/manage/deactivate", request, String.class);
                } catch (RestClientException e) {
                    log.error("an error occured while triggering deactivate on worker " + last + " from " + consumerState.getProcessDefinition(), e.getMessage());
                }
                newState.getRegistryWorkers().remove(last);

            }
            consumerStateRepository.save(newState);
        }
    }

    // Internal apis
    private RegistryWorker getWorkerAvailable(WorkerType workerType, Set<String> alreadyAssignedWorkers) throws Exception {
        Random random = new Random();
        List<RegistryWorker> availableWorkers = workerRepository.findAll().stream()
                .filter(e -> e.getWorkerType() == workerType)
                .filter(e -> e.getStatus() == StatusWorker.OK)
                .filter(e -> !alreadyAssignedWorkers.contains(e.getFQDN()))
                .collect(Collectors.toList());
        int skip = availableWorkers.size() - 1 > 0 ? random.nextInt(availableWorkers.size() - 1) : 0;
        return availableWorkers.stream()
                .skip(skip)
                .findFirst().orElseThrow(() -> new Exception("No Worker Available"));
    }

    private void triggerAction(ConsumerState consumerState, String action, StatusProcess statusIfOk, StatusProcess statusIfKo) {
        boolean hasErrors = consumerState.getStatusProcess() == StatusProcess.ERROR;
        for (String workerFQDN : consumerState.getRegistryWorkers()) {
            RegistryWorker worker = workerRepository.findByKey(workerFQDN);
            log.info("triggering {} on {}", action, consumerState.getProcessDefinition());
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<ProcessDefinition> request = new HttpEntity<>(consumerState.getProcessDefinition());
                restTemplate.postForObject(worker.getBaseUrl() + "/manage/" + action, request, String.class);
            } catch (RestClientException e) {
                log.error("an error occured while triggering" + action + " on " + consumerState.getProcessDefinition(), e.getMessage());
                hasErrors = true;
            }
        }

        ConsumerState newState;
        if (!hasErrors) {
            newState = consumerState.withStatusProcess(statusIfOk);
            if ("deactivate".equals(statusIfOk)) {
                newState.getRegistryWorkers().clear();
            }
        } else {
            newState = consumerState.withStatusProcess(statusIfKo);
        }
        consumerStateRepository.save(newState);
    }

    private ConsumerState assignConsumerToWorkers(ConsumerState consumerState) {
        StatusProcess result = StatusProcess.INIT;
        try {
            int nbWorkerToAssign = consumerState.getNbInstance() - consumerState.getRegistryWorkers().size();
            for (int i = 0; i < nbWorkerToAssign; i++) {
                RegistryWorker workerAvailable = getWorkerAvailable(consumerState.getWorkerType(), consumerState.getRegistryWorkers());
                consumerState.getRegistryWorkers().add(workerAvailable.getFQDN());
            }

        } catch (Exception e) {
            log.error("No Worker available for {}", consumerState.getProcessDefinition());
            result = StatusProcess.ERROR;
        }
        ConsumerState newState = consumerState.withStatusProcess(result);
        consumerStateRepository.save(newState);

        return newState;
    }

    @Scheduled(initialDelay = 1 * 60 * 1000, fixedRate = 1 * 60 * 1000)
    public void checkWorkersAlive() {
        log.info("checkWorkersAlive");
        List<RegistryWorker> workers = workerRepository.findAll();
        workers.stream()
                .filter(registry -> registry.getStatus() == StatusWorker.OK)
                .forEach(registry -> {
                    registry.setStatus(statusWorker(registry.getDateRefresh(), registry));
                    workerRepository.save(registry);
                });
        rescheduleConsumerFromDeadWorkers();
        rescheduleConsumersInError();
        workerOK.set(
                workers.stream()
                        .filter(registryWorker -> registryWorker.getStatus() == StatusWorker.OK)
                        .count()
        );

        workerKO.set(
                workers.stream()
                        .filter(registryWorker -> registryWorker.getStatus() == StatusWorker.KO)
                        .count()
        );

        workerFULL.set(
                workers.stream()
                        .filter(registryWorker -> registryWorker.getStatus() == StatusWorker.FULL)
                        .count()
        );
    }

    private void rescheduleConsumerFromDeadWorkers() {
        workerRepository.findAll()
                .stream()
                .filter(worker -> worker.getStatus() == StatusWorker.KO)
                .forEach(this::rescheduleConsumerFromDeadWorker);
    }

    private void rescheduleConsumerFromDeadWorker(RegistryWorker registryWorker) {
        List<StatusConsumer> consumerList = registryWorker.getStatusConsumerList();
        for (StatusConsumer statusConsumer : consumerList) {
            ConsumerState consumerState = consumerStateRepository.findByKey(statusConsumer.getIdProcessConsumer());
            //remove it from running workers
            consumerState.getRegistryWorkers().remove(registryWorker.getFQDN());
            //elect new worker
            rescheduleProcessDefinition(consumerState);
        }
    }

    private void rescheduleProcessDefinition(ConsumerState consumerState) {
        log.info("rescheduling {}", consumerState.getProcessDefinition());
        //run it
        activate(consumerState.getProcessDefinition());
    }

    private void rescheduleConsumersInError() {
        consumerStateRepository.findAll().stream()
                .filter(consumerState -> consumerState.getStatusProcess() == StatusProcess.ERROR || consumerState.getStatusProcess() == StatusProcess.DEGRADED)
                .forEach(this::rescheduleProcessDefinition);
    }

    private StatusWorker statusWorker(Date lastRefresh, RegistryWorker registryWorker) {
        //too many consumer
        if (registryWorker.getStatusConsumerList() != null && registryWorker.getStatusConsumerList().size() > processConfiguration.getMaxProcessConsumer()) {
            return StatusWorker.FULL;
        }
        Date actual = new Date();
        LocalDateTime lActual = LocalDateTime.ofInstant(actual.toInstant(), ZoneId.systemDefault());
        LocalDateTime lastCurrent = LocalDateTime.ofInstant(lastRefresh.toInstant(), ZoneId.systemDefault());
        if (lastCurrent.plusMinutes(5).plusSeconds(10).isBefore(lActual)) {
            return StatusWorker.KO;
        }
        return StatusWorker.OK;
    }
}
