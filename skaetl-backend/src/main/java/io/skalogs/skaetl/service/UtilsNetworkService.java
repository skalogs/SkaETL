package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.web.domain.NetworkLinksWeb;
import io.skalogs.skaetl.web.domain.NetworkNodeWeb;
import io.skalogs.skaetl.web.domain.NetworkWeb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class UtilsNetworkService {

    //private final ConfService confService;
    private final RegistryService registryService;

    public UtilsNetworkService(ConfService confService, RegistryService registryService) {
        //this.confService = confService;
        this.registryService = registryService;
    }

    public NetworkWeb viewNetwork(){
        List<NetworkLinksWeb> consumerListLink = new ArrayList<>();
        Map<String, NetworkNodeWeb> consumerMapNode = new HashMap<>();
        List<NetworkLinksWeb> metricListLink = new ArrayList<>();
        Map<String, NetworkNodeWeb> metricMapNode = new HashMap<>();

        // Consumer processes
        registryService.findAll(WorkerType.PROCESS_CONSUMER).stream()
                .filter(consumerState -> consumerState.getStatusProcess() != StatusProcess.CREATION)
                .map(consumerState -> (ProcessConsumer) consumerState.getProcessDefinition())
                .collect(toList())
                .stream()
                .forEach(processConsumer -> addConsumerNodeLink(processConsumer, consumerMapNode, consumerListLink));

        // Metric processes
        registryService.findAll(WorkerType.METRIC_PROCESS).stream()
                .filter(metricState -> metricState.getStatusProcess() != StatusProcess.CREATION)
                .map(metricState -> (ProcessMetric) metricState.getProcessDefinition())
                .collect(toList())
                .stream()
                .forEach(processMetric -> addMetricNodeLink(processMetric, metricMapNode, metricListLink));

        return NetworkWeb.builder()
                .consumerLinksList(consumerListLink)
                .consumerNodeList(consumerMapNode.values().stream().collect(toList()))
                .metricLinksList(metricListLink)
                .metricNodeList(metricMapNode.values().stream().collect(toList()))
                .build();

    }

    private void addConsumerNodeLink(ProcessConsumer processConsumer, Map<String, NetworkNodeWeb> mapNode, List<NetworkLinksWeb> listLink) {

        String source, target;

        //add node Input
        source = new StringBuilder().append("TOPIC [").append(processConsumer.getProcessInput().getTopicInput()).append("]").toString();
        mapNode.put(source, NetworkNodeWeb.builder()
                .id(source)
                .name(source)
                .color(getColor(TypeOutput.KAFKA))
                .build());

        //add node Output
        for (ProcessOutput processOutput : processConsumer.getProcessOutput()) {

            if (processOutput.getTypeOutput() == TypeOutput.KAFKA)
                target = new StringBuilder().append("TOPIC [").append(processOutput.getParameterOutput().getTopicOut()).append("]").toString();
            else
                target = processOutput.getTypeOutput().name();

            mapNode.put(target, NetworkNodeWeb.builder()
                    .id(target)
                    .name(target)
                    .color(getColor(processOutput.getTypeOutput()))
                    .build());

            //add link
            listLink.add(NetworkLinksWeb.builder()
                    .id(source + target)
                    .sid(source)
                    .tid(target)
                    .color("green")
                    .name(processConsumer.getName())
                    .build());
        }
    }

    private void addMetricNodeLink(ProcessMetric processMetric, Map<String, NetworkNodeWeb> mapNode, List<NetworkLinksWeb> listLink) {

        String source, target;

        // Adds input nodes
        source = new StringBuilder().append("TOPIC [").append(processMetric.getFromTopic()).append("]").toString();
        mapNode.put(source, NetworkNodeWeb.builder()
                .id(source)
                .name(source)
                .color(getColor(TypeOutput.KAFKA))
                .build());

        // Adds output nodes
        for (ProcessOutput processOutput : processMetric.getProcessOutputs()) {

            if (processOutput.getTypeOutput() == TypeOutput.KAFKA)
                target = new StringBuilder().append("TOPIC [").append(processOutput.getParameterOutput().getTopicOut()).append("]").toString();
            else
                target = processOutput.getTypeOutput().name();

            mapNode.put(target, NetworkNodeWeb.builder()
                    .id(target)
                    .name(target)
                    .color(getColor(processOutput.getTypeOutput()))
                    .build());

            // Adds links
            listLink.add(NetworkLinksWeb.builder()
                    .id(source + target)
                    .sid(source)
                    .tid(target)
                    .color("red")
                    .name(processMetric.getName())
                    .build());
        }
    }

    private String getColor(TypeOutput typeOutput) {
        switch (typeOutput) {
            case KAFKA:
                return "#e9a820";
            case ELASTICSEARCH:
                return "#6ecadc";
            case SLACK:
                return "#3eb991";
            case EMAIL:
                return "#e01563";
            default:
                return "white";
        }
    }
}
