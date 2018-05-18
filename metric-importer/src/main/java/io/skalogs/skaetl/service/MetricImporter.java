package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.rules.metrics.GenericMetricProcessor;
import io.skalogs.skaetl.rules.metrics.RuleMetricExecutor;
import io.skalogs.skaetl.serdes.GenericSerdes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.*;

import static io.skalogs.skaetl.domain.ProcessConstants.TOPIC_TREAT_PROCESS;
import static java.util.stream.Collectors.toList;

@Slf4j
@AllArgsConstructor
@Component
public class MetricImporter {
    private final RuleMetricExecutor ruleMetricExecutor;
    private final KafkaConfiguration kafkaConfiguration;
    private final ProcessConfiguration processConfiguration;
    private final KafkaAdminService kafkaAdminService;
    private final ApplicationContext applicationContext;
    private final Map<ProcessMetric, List<KafkaStreams>> runningMetricProcessors = new HashMap();

    @PostConstruct
    public void init() {
        sendToRegistry("addService");
    }

    public void activate(ProcessMetric processMetric) {
        if (runningMetricProcessors.containsKey(processMetric)) {
            log.info("stopping old version of {} Metric Stream Process", processMetric.getName());
            deactivate(processMetric);
        }
        log.info("creating {} Metric Stream Process", processMetric.getName());
        kafkaAdminService.buildTopic(processMetric.getFromTopic());
        processMetric.getProcessOutputs()
                .stream()
                .filter(processOutput -> processOutput.getTypeOutput() == TypeOutput.KAFKA)
                .forEach(processOutput -> kafkaAdminService.buildTopic(processOutput.getParameterOutput().getTopicOut()));


        List<KafkaStreams> streams = new ArrayList<>();
        for (String idProcessConsumer : processMetric.getSourceProcessConsumers()) {
            streams.add(feedMergeTopic(idProcessConsumer,processMetric.getFromTopic()));
        }

        if (!processMetric.getSourceProcessConsumersB().isEmpty()) {
            kafkaAdminService.buildTopic(processMetric.getFromTopicB());
            for (String idProcessConsumer : processMetric.getSourceProcessConsumersB()) {
                streams.add(feedMergeTopic(idProcessConsumer, processMetric.getFromTopicB()));
            }
        }

        GenericMetricProcessor metricProcessor = ruleMetricExecutor.instanciate(processMetric);
        metricProcessor.setApplicationContext(applicationContext);

        KafkaStreams metricStream = metricProcessor.buildStream(createProperties(kafkaConfiguration.getBootstrapServers()));
        metricStream.start();

        streams.add(metricStream);

        ProcessMetric processMetricDefinition = processMetric.withTimestamp(new Date());
        runningMetricProcessors.put(processMetricDefinition, streams);
    }

    private KafkaStreams feedMergeTopic(String id, String mergeTopic) {

        StreamsBuilder builder = new StreamsBuilder();
        Properties properties = createProperties(kafkaConfiguration.getBootstrapServers());
        String inputTopic = id + TOPIC_TREAT_PROCESS;
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, inputTopic + "merger-stream");

        KStream<String, JsonNode> stream = builder.stream(inputTopic, Consumed.with(Serdes.String(), GenericSerdes.jsonNodeSerde()));
        stream.to(mergeTopic, Produced.with(Serdes.String(),GenericSerdes.jsonNodeSerde()));

        final KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
        return streams;

    }

    public void deactivate(ProcessMetric processMetric) {
        log.info("deactivating {} Metric Stream Process", processMetric.getName());
        if (runningMetricProcessors.containsKey(processMetric)) {
            runningMetricProcessors.get(processMetric).forEach((stream) -> stream.close());
            runningMetricProcessors.remove(processMetric);
        }
    }

    private Properties createProperties(String bootstrapServers) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return props;
    }

    private void sendToRegistry(String action) {
        RegistryWorker registry = null;
        try {
            registry = RegistryWorker.builder()
                    .workerType(WorkerType.METRIC_PROCESS)
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
        return runningMetricProcessors.keySet().stream()
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

}
