package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.serdes.GenericSerdes;
import io.skalogs.skaetl.service.referential.ReferentialESService;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
@Lazy(value = false)
@Slf4j
@AllArgsConstructor
public class ReferentialImporter {

    private final KafkaAdminService kafkaAdminService;
    private final KafkaConfiguration kafkaConfiguration;
    private final ProcessConfiguration processConfiguration;
    private final Map<ProcessReferential, List<KafkaStreams>> runningProcessReferential = new HashMap();
    private final Map<ProcessReferential, List<KafkaStreams>> runningMergeProcess = new HashMap();
    private final Map<ProcessReferential, List<ReferentialService>> runningService = new HashMap();
    public static final String TOPIC_PARSED_PROCESS = "parsedprocess";
    public static final String TOPIC_MERGE_REFERENTIAL = "mergereferential";

    @PostConstruct
    public void init() {
        sendToRegistry("addService");
    }

    public void activate(ProcessReferential processReferential) {
        if (StringUtils.isNotBlank(processReferential.getIdProcess())) {
            String topicMerge = TOPIC_MERGE_REFERENTIAL + "-" + processReferential.getIdProcess();
            kafkaAdminService.buildTopic(topicMerge);
            runningMergeProcess.put(processReferential, new ArrayList<>());
            runningProcessReferential.put(processReferential, new ArrayList<>());
            runningService.put(processReferential, new ArrayList<>());
            processReferential.getListIdProcessConsumer().stream().forEach(consumerId -> feedStream(consumerId, processReferential, topicMerge));
            // treat the merge topic
            log.info("creating {} Process Referential", processReferential.getName());
            buildStreamMerge(processReferential, topicMerge);
        } else {
            log.error("No Referential Id for processReferential {}", processReferential);
        }
    }

    private void feedStream(String consumerId, ProcessReferential processReferential, String topicMerge) {
        String topicSource = consumerId + TOPIC_PARSED_PROCESS;
        log.info("creating {} Process Merge for topicsource {}", consumerId, topicSource);
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, JsonNode> streamToMerge = builder.stream(topicSource, Consumed.with(Serdes.String(), GenericSerdes.jsonNodeSerde()));
        streamToMerge.to(topicMerge, Produced.with(Serdes.String(), GenericSerdes.jsonNodeSerde()));
        KafkaStreams streams = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties(processReferential.getIdProcess() +"#" +consumerId + "#merge-topic", kafkaConfiguration.getBootstrapServers()));
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        runningMergeProcess.get(processReferential).add(streams);
        streams.start();
    }

    public void deactivate(ProcessReferential processReferential) {
        log.info("deactivating {} Process Merge", processReferential.getName());
        runningMergeProcess.get(processReferential.getIdProcess()).stream()
                .forEach(stream -> stream.close());
        log.info("deactivating {} Process Referential", processReferential.getName());
        runningProcessReferential.get(processReferential).stream()
                .forEach(stream -> stream.close());
        runningProcessReferential.remove(processReferential);
        runningService.remove(processReferential);
    }

    private void buildStreamMerge(ProcessReferential processReferential, String topicMerge) {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, JsonNode> streamToRef = builder.stream(topicMerge, Consumed.with(Serdes.String(), GenericSerdes.jsonNodeSerde()));
        ReferentialProcessor referentialProcessor = new ReferentialProcessor(processReferential, kafkaConfiguration);
        streamToRef.process(() -> referentialProcessor);
        runningService.get(processReferential).add(referentialProcessor);
        KafkaStreams stream = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties(processReferential.getIdProcess() + "#" + TOPIC_MERGE_REFERENTIAL, kafkaConfiguration.getBootstrapServers()));
        Runtime.getRuntime().addShutdownHook(new Thread(stream::close));
        runningProcessReferential.get(processReferential).add(stream);
        stream.start();
    }

    private void sendToRegistry(String action) {
        RegistryWorker registry = null;
        try {
            registry = RegistryWorker.builder()
                    .workerType(WorkerType.REFERENTIAL_PROCESS)
                    .ip(InetAddress.getLocalHost().getHostName())
                    .name(InetAddress.getLocalHost().getHostName())
                    .port(processConfiguration.getPortClient())
                    .statusConsumerList(statusExecutor())
                    .build();
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<RegistryWorker> request = new HttpEntity<>(registry);
            String url = processConfiguration.getUrlRegistry();
            String res = restTemplate.postForObject(url + "/process/registry/" + action, request, String.class);
            log.debug("sendToRegistry result {}", res);
        } catch (Exception e) {
            log.error("Exception on sendToRegistry", e);
        }

    }

    public List<StatusConsumer> statusExecutor() {
        return runningProcessReferential.keySet().stream()
                .map(e -> StatusConsumer.builder()
                        .statusProcess(StatusProcess.ENABLE)
                        .creation(e.getTimestamp())
                        .idProcessConsumer(e.getIdProcess())
                        .build())
                .collect(toList());
    }

    @Scheduled(initialDelay = 20 * 1000, fixedRate = 5 * 60 * 1000)
    public void refresh() {
        sendToRegistry("refresh");
    }

    @Scheduled(initialDelay = 1 * 60 * 1000, fixedRate = 5 * 60 * 1000)
    public void flush(){
        runningService.values().stream().forEach(
                referentialServices -> referentialServices.stream()
                        .forEach(referentialService -> referentialService.flush()));
    }
}

