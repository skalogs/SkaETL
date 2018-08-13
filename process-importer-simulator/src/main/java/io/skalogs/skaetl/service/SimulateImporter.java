package io.skalogs.skaetl.service;

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
