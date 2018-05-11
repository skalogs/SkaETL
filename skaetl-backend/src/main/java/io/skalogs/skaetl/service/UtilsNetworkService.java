package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.web.domain.NetworkLinksWeb;
import io.skalogs.skaetl.web.domain.NetworkNodeWeb;
import io.skalogs.skaetl.web.domain.NetworkWeb;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class UtilsNetworkService {

    private final ConfService confService;
    private final RegistryService registryService;

    public UtilsNetworkService(ConfService confService, RegistryService registryService) {
        this.confService = confService;
        this.registryService = registryService;
    }

    public NetworkWeb viewNetwork(){
        List<NetworkLinksWeb> listLink = new ArrayList<>();
        Map<String,NetworkNodeWeb> mapNode = new HashMap<>();
        //Process Consumer
        registryService.findAll(WorkerType.PROCESS_CONSUMER).stream()
                .filter(consumerState -> consumerState.getStatusProcess() != StatusProcess.CREATION)
                .map(consumerState -> (ProcessConsumer) consumerState.getProcessDefinition())
                .collect(toList())
                .stream()
                .forEach(processConsumer -> addNodeLink(processConsumer,mapNode,listLink));
        return NetworkWeb.builder()
                .linksList(listLink)
                .nodeList(mapNode.values().stream().collect(toList()))
                .build();

    }

    private String addParser(ProcessConsumer processConsumer, Map<String,NetworkNodeWeb> mapNode, List<NetworkLinksWeb> listLink,String prevId){
        //add Parser
        if(processConsumer.getProcessParser()!=null && !processConsumer.getProcessParser().isEmpty()){
            for( ProcessParser processParser : processConsumer.getProcessParser()){
                mapNode.put(processConsumer.getName()+"-"+processParser.getTypeParser().name(),NetworkNodeWeb.builder()
                        .id(processConsumer.getName()+"-"+processParser.getTypeParser().name())
                        .name("Parser "+processParser.getTypeParser().name()+"-"+processConsumer.getName())
                        .color("blue")
                        .build());
                listLink.add(NetworkLinksWeb.builder()
                        .sid(prevId)
                        .tid(processConsumer.getName()+"-"+processParser.getTypeParser().name())
                        .color("green")
                        .build());
                if(processParser.getActiveFailForward()!=null && StringUtils.isNotBlank(processParser.getFailForwardTopic())){
                    mapNode.put(processParser.getFailForwardTopic(),NetworkNodeWeb.builder()
                            .id(processParser.getFailForwardTopic())
                            .name(processParser.getFailForwardTopic())
                            .color("orange")
                            .build());
                    listLink.add(NetworkLinksWeb.builder()
                            .sid(processConsumer.getName()+"-"+processParser.getTypeParser().name())
                            .tid(processParser.getFailForwardTopic())
                            .name("error")
                            .color("red")
                            .build());
                }
                prevId=processConsumer.getName()+"-"+processParser.getTypeParser().name();
            }
        }
        return prevId;
    }

    private void addNodeLink(ProcessConsumer processConsumer, Map<String,NetworkNodeWeb> mapNode, List<NetworkLinksWeb> listLink){
        //add node Input
        mapNode.put(processConsumer.getProcessInput().getTopicInput(),NetworkNodeWeb.builder()
                .id(processConsumer.getProcessInput().getTopicInput())
                .name(processConsumer.getProcessInput().getTopicInput()+"-"+processConsumer.getName())
                .color("orange")
                .build());
        String prevId = processConsumer.getProcessInput().getTopicInput();

        prevId= addParser(processConsumer,mapNode,listLink,prevId);

        //add node Output
        for(ProcessOutput processOutput : processConsumer.getProcessOutput()){
            mapNode.put(processOutput.getTypeOutput().name(),NetworkNodeWeb.builder()
                    .id(processConsumer.getName()+"-"+processOutput.getTypeOutput().name())
                    .name(processOutput.getTypeOutput().name()+"-"+processConsumer.getName())
                    .color(processOutput.getTypeOutput() == TypeOutput.KAFKA ? "orange" : "black")
                    .build());

            //add link
            listLink.add(NetworkLinksWeb.builder()
                    .sid(prevId)
                    .tid(processConsumer.getName()+"-"+processOutput.getTypeOutput().name())
                    .color("green")
                    .name("output")
                    .build());

        }

    }



}
