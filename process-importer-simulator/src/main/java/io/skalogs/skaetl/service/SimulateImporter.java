package io.skalogs.skaetl.service;

/*-
 * #%L
 * process-importer-simulator
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

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.config.RegistryConfiguration;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.ProcessFilter;
import io.skalogs.skaetl.rules.filters.GenericFilter;
import io.skalogs.skaetl.rules.filters.RuleFilterExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static io.skalogs.skaetl.domain.ProcessConstants.SIMULATE_OUTPUT;


@Component
@Lazy(value = false)
@Slf4j
public class SimulateImporter extends AbstractGenericImporter {

    private final RuleFilterExecutor ruleExecutor;
    private final KafkaAdminService kafkaAdminService;

    public SimulateImporter(GenericValidator genericValidator, GenericTransformator transformValidator, GenericParser genericParser, GenericFilterService genericFilterService, RuleFilterExecutor ruleExecutor, KafkaAdminService kafkaAdminService, ProcessConfiguration processConfiguration, ExternalHTTPService externalHTTPService, RegistryConfiguration registryConfiguration) {
        super(genericValidator, transformValidator, genericParser, genericFilterService, processConfiguration, externalHTTPService, registryConfiguration);
        this.ruleExecutor = ruleExecutor;
        this.kafkaAdminService = kafkaAdminService;
    }

    public void createProcessGeneric(ProcessConsumer processConsumer) {
        log.info("Create topic for importer");
        kafkaAdminService.buildTopic(SIMULATE_OUTPUT);
        log.info("Create process importer {}", processConsumer.getName());
        List<GenericFilter> genericFilters = new ArrayList<>();
        for (ProcessFilter processFilter : processConsumer.getProcessFilter()) {
            genericFilters.add(ruleExecutor.instanciate(processFilter.getName(), processFilter.getCriteria(), processFilter));
        }
        SimulateStreamService simulateStreamService = new SimulateStreamService(
                getGenericValidator(),
                getGenericTransformator(),
                getGenericParser(),
                getGenericFilterService(),
                processConsumer,
                genericFilters);
        getRunningConsumers().put(processConsumer, simulateStreamService);
        getExecutor().submit(simulateStreamService);
    }

}
