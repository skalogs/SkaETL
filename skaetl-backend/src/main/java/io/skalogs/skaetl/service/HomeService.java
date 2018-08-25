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

import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.domain.stat.*;
import io.skalogs.skaetl.web.domain.DataCharts;
import io.skalogs.skaetl.web.domain.DataUnitCharts;
import io.skalogs.skaetl.web.domain.HomeWeb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

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

    private DataUnitCharts buildChartsProcess(ConsumerState consumerState) {
        String name =consumerState.getProcessDefinition().getName();
        return DataUnitCharts.builder()
                .borderColor(randomColor())
                .label("Input "+ name)
                .data(promservice.fetchDataCapture("skaetl_nb_read_kafka_count", "processConsumerName", name, 5))
                .build();
    }

    public HomeWeb getHome() {
        return HomeWeb.builder()
                .numberProcessTotal(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().count())
                .numberProcessActive(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.ENABLE).count())
                .numberProcessDeActive(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.DISABLE).count())
                .numberProcessError(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.ERROR).count())
                .numberProcessInit(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.INIT).count())
                .numberProcessDegraded(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.DEGRADED).count())
                .numberProcessCreation(registryService.findAll(WorkerType.PROCESS_CONSUMER).stream().filter(e -> e.getStatusProcess() == StatusProcess.CREATION).count())
                .listStatProcess(buildListStatProcess())
                .numberMetricTotal(registryService.findAll(WorkerType.METRIC_PROCESS).stream().count())
                .numberMetricActive(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ENABLE).count())
                .numberMetricDeActive(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.DISABLE).count())
                .numberMetricError(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ERROR).count())
                .numberMetricInit(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.INIT).count())
                .numberMetricDegraded(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.DEGRADED).count())
                .numberMetricCreation(registryService.findAll(WorkerType.METRIC_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.CREATION).count())
                .listStatMetric(buildListStatMetric())
                .numberReferentialTotal(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().count())
                .numberReferentialActive(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ENABLE).count())
                .numberReferentialDeActive(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.DISABLE).count())
                .numberReferentialError(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.ERROR).count())
                .numberReferentialInit(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.INIT).count())
                .numberReferentialDegraded(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.DEGRADED).count())
                .numberReferentialCreation(registryService.findAll(WorkerType.REFERENTIAL_PROCESS).stream().filter(e -> e.getStatusProcess() == StatusProcess.CREATION).count())
                .listStatReferential(buildListStatReferential())
                .numberConfigurationTotal(confService.findAll().stream().count())
                .numberConfigurationActive(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.ACTIVE).count())
                .numberConfigurationDeActive(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.DISABLE).count())
                .numberConfigurationError(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.ERROR).count())
                .numberConfigurationInit(confService.findAll().stream().filter(e -> e.statusConfig == StatusConfig.INIT).count())
                .listStatConfiguration(buildListStatConfiguration())
                .numberWorkerTotal(registryService.getAllStatus().stream().count())
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
                .nbRead(promservice.fetchData("skaetl_nb_read_kafka_count", "processConsumerName", name, 5))
                .nbOutput(promservice.fetchData("skaetl_nb_transformation_validation_count", "processConsumerName", name, 5))
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
                .nbInput(promservice.fetchData("skaetl_nb_metric_input", "metricConsumerName", name, 5))
                .nbInput(promservice.fetchData("skaetl_nb_metric_output", "metricConsumerName", name, 5))
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
                .nbInput(promservice.fetchData("skaetl_nb_referential_input", "referentialConsumerName", name, 5))
                .nbInput(promservice.fetchData("skaetl_nb_referentialmetric_output", "referentialConsumerName", name, 5))
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
