package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.domain.stat.*;
import io.skalogs.skaetl.web.domain.DataCharts;
import io.skalogs.skaetl.web.domain.DataUnitCharts;
import io.skalogs.skaetl.web.domain.HomeWeb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class HomeService {

    private final RegistryService registryService;
    private final ConfService confService;
    private final PromService promservice;
    private final ConfSkalogsService confSkalogsService;

    public HomeService(RegistryService registryService, ConfService confService, PromService promservice,ConfSkalogsService confSkalogsService) {
        this.registryService = registryService;
        this.confService = confService;
        this.promservice = promservice;
        this.confSkalogsService = confSkalogsService;
    }

    public DataCharts chartsForProcess() {
        log.info("loading chartsForProcess ");
        return DataCharts.builder()
                .datasets(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream()
                        .filter(e -> e.getStatusProcess() == StatusProcess.ENABLE)
                        .map(e -> buildChartsProcess(e))
                        .collect(toList()))
                .build();
    }

    public DataCharts chartsForClients(){
        log.info("loading chartsForClients ");
        List<DataUnitCharts> data = new ArrayList<>();
        data.add(buildchartsForClients());
        for(String elem :confSkalogsService.tabEnv){
            data.add(buildchartsForClientsKey("env",elem));
        }
        return DataCharts.builder()
                .datasets(data)
                .build();
    }

    private DataUnitCharts buildchartsForClients(){
        return DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("All client")
                .data(promservice.fetchDataCapture("fetch_skalogs_conf", null, null, 5))
                .build();
    }

    private DataUnitCharts buildchartsForClientsKey(String key, String value){
        return DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("Client for "+value)
                .data(promservice.fetchDataCapture("fetch_skalogs_conf", key, value, 5))
                .build();
    }

    public DataCharts chartsForMetrics() {
        log.info("loading chartsForMetrics ");
        return DataCharts.builder()
                .datasets(registryService.findAll(WorkerType.METRIC_PROCESS).stream()
                        .filter(e -> e.getStatusProcess() == StatusProcess.ENABLE)
                        .map(e -> buildChartsMetric(e))
                        .flatMap(e -> e.stream())
                        .collect(Collectors.toList()))
                .build();
    }

    private DataUnitCharts buildChartsProcess(ConsumerState consumerState) {
        String name =consumerState.getProcessDefinition().getName();
        return DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("Input "+ name)
                .data(promservice.fetchDataCapture("nb_read_kafka_count", "processConsumerName", name, 5))
                .build();
    }

    private List<DataUnitCharts> buildChartsMetric(ConsumerState consumerState) {
        String name = consumerState.getProcessDefinition().getName();
        List<DataUnitCharts> charts = new ArrayList<>();
        charts.add(DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("Input "+name)
                .data(promservice.fetchDataCapture("nb_metric_input", "metricConsumerName", name, 5))
                .build());
        charts.add(DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("Output "+name)
                .data(promservice.fetchDataCapture("nb_metric_output", "metricConsumerName", name, 5))
                .build());
        return charts;
    }

    private List<DataUnitCharts> buildChartsReferential(ConsumerState consumerState) {
        String name = consumerState.getProcessDefinition().getName();
        List<DataUnitCharts> charts = new ArrayList<>();
        charts.add(DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("Input " + name)
                .data(promservice.fetchDataCapture("nb_referential_input", "referentialConsumerName", name, 5))
                .build());
        charts.add(DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("Output " + name)
                .data(promservice.fetchDataCapture("nb_referential_output", "referentialConsumerName", name, 5))
                .build());
        return charts;
    }

    public HomeWeb getHome() {
        return HomeWeb.builder()
                .numberProcessActive(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.ENABLE).count())
                .numberProcessDeActive(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.DISABLE).count())
                .numberProcessError(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.ERROR).count())
                .numberProcessInit(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.INIT).count())
                .listStatProcess(buildListStatProcess())
                .numberMetricActive(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ENABLE).count())
                .numberMetricDeActive(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.DISABLE).count())
                .numberMetricError(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ERROR).count())
                .numberMetricInit(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.INIT).count())
                .listStatMetric(buildListStatMetric())
                .numberReferentialActive(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ENABLE).count())
                .numberReferentialDeActive(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.DISABLE).count())
                .numberReferentialError(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ERROR).count())
                .numberReferentialInit(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.INIT).count())
                .listStatReferential(buildListStatReferential())
                .numberConfigurationActive(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.ACTIVE).count())
                .numberConfigurationDeActive(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.DISABLE).count())
                .numberConfigurationError(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.ERROR).count())
                .numberConfigurationInit(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.INIT).count())
                .listStatConfiguration(buildListStatConfiguration())
                .numberWorkerMetric(registryService.getAllStatus().stream().filter(e -> e.getWorkerType() == WorkerType.METRIC_PROCESS).count())
                .numberWorkerProcess(registryService.getAllStatus().stream().filter(e -> e.getWorkerType() == WorkerType.PROCESS_CONSUMER).count())
                .numberWorkerReferential(registryService.getAllStatus().stream().filter(e -> e.getWorkerType() == WorkerType.REFERENTIAL_PROCESS).count())
                .listStatWorker(buildListStatWorker())
                .listStatClient(confSkalogsService.findClientLogstash())
                .build();
    }

    private List<StatProcess> buildListStatProcess() {
        return registryService.findAll(WorkerType.PROCESS_CONSUMER).stream()
                .map(e -> buildStatProcess(e))
                .collect(toList());
    }

    private StatProcess buildStatProcess(ConsumerState consumerState) {
        String name = consumerState.getProcessDefinition().getName();
        return StatProcess.builder()
                .name(name)
                .status(consumerState.getStatusProcess().name())
                .nbRead(promservice.fetchData("nb_read_kafka_count", "processConsumerName", name, 5))
                .nbOutput(promservice.fetchData("nb_transformation_validation_count", "processConsumerName", name, 5))
                .build();
    }

    private List<StatMetric> buildListStatMetric() {
        return registryService.findAll(WorkerType.METRIC_PROCESS).stream()
                .map(e -> buildStatMetric(e))
                .collect(toList());
    }

    private StatMetric buildStatMetric(ConsumerState consumerState) {
        String name = consumerState.getProcessDefinition().getName();
        return StatMetric.builder()
                .name(name)
                .status(consumerState.getStatusProcess().name())
                .nbInput(promservice.fetchData("nb_metric_input", "metricConsumerName", name, 5))
                .nbInput(promservice.fetchData("nb_metric_output", "metricConsumerName", name, 5))
                .build();
    }

    private List<StatReferential> buildListStatReferential() {
        return registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream()
                .map(e -> buildStatReferential(e))
                .collect(toList());
    }

    private StatReferential buildStatReferential(ConsumerState consumerState) {
        String name = consumerState.getProcessDefinition().getName();
        return StatReferential.builder()
                .name(name)
                .status(consumerState.getStatusProcess().name())
                .nbInput(promservice.fetchData("nb_referential_input", "referentialConsumerName", name, 5))
                .nbInput(promservice.fetchData("nb_referentialmetric_output", "referentialConsumerName", name, 5))
                .build();
    }

    private List<StatConfiguration> buildListStatConfiguration() {
        return confService.findAll().stream()
                .map(e -> buildStatConfiguration(e))
                .collect(toList());
    }

    private StatConfiguration buildStatConfiguration(ConfigurationLogstash configurationLogstash) {
        return StatConfiguration.builder()
                .name(configurationLogstash.getName())
                .status(configurationLogstash.getStatusConfig().name())
                .build();
    }

    private List<StatWorker> buildListStatWorker() {
        return registryService.getAllStatus().stream()
                .map(e -> buildStatWorker(e))
                .collect(toList());
    }

    private StatWorker buildStatWorker(RegistryWorker registryWorker) {
        return StatWorker.builder()
                .name(registryWorker.getName())
                .ip(registryWorker.getIp())
                .nbProcess(Long.valueOf(registryWorker.getStatusConsumerList().size()))
                .type(registryWorker.getWorkerType().name())
                .build();
    }
    private String[] tabColor = new String[]{"rgba(187,222,251,1)","rgba(255,245,157,1)","rgba(255,204,128,1)","rgba(207,216,220,1)"};
    private String randomColor() {
        return tabColor[RANDOM.nextInt(tabColor.length)];
    }
    private static final Random RANDOM = new Random();
}
