package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.config.RegistryConfiguration;
import io.skalogs.skaetl.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class AbstractGenericImporter {


    private static final int NUM_CONSUMERS = 10;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_CONSUMERS);
    private final List<AbstractStreamProcess> listConsumer = new ArrayList<>();
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
        getListConsumer().stream()
                .forEach(processStream -> externalHTTPService.revokeCache(processStream.getProcessConsumer()));

        // Shutdown all kafkastreams
        getListConsumer().stream()
                .forEach(processStream -> processStream.shutdownAllStreams());

        // Remove the process Consumer
        getListConsumer().clear();

        // Update the registry
        sendToRegistry("refresh");
    }

    public void disable(ProcessConsumer processConsumer) {
        externalHTTPService.revokeCache(processConsumer);

        // Shutdown all kafkastreams
        getListConsumer().stream()
                .filter(processStream -> processStream.getProcessConsumer().getIdProcess().equals(processConsumer.getIdProcess()))
                .forEach(processStream -> processStream.shutdownAllStreams());

        // Remove the process Consumer
        getListConsumer().removeIf(processStream -> processStream.getProcessConsumer().getIdProcess().equals(processConsumer.getIdProcess()));

        // Update the registry
        sendToRegistry("refresh");
    }

    public List<StatusConsumer> statusExecutor() {
        return listConsumer.stream()
                .map(e -> StatusConsumer.builder()
                        .statusProcess(StatusProcess.ENABLE)
                        .creation(e.getProcessConsumer().getTimestamp())
                        .idProcessConsumer(e.getProcessConsumer().getIdProcess())
                        .build())
                .collect(toList());
    }


}
