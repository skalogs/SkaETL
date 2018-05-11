package io.skalogs.skaetl.service;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.ProcessConfiguration;
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

    public SimulateImporter(GenericValidator genericValidator, GenericTransformator transformValidator, GenericParser genericParser, RuleFilterExecutor ruleExecutor, KafkaAdminService kafkaAdminService, ProcessConfiguration processConfiguration, ExternalHTTPService externalHTTPService) {
        super(genericValidator, transformValidator, genericParser, processConfiguration, externalHTTPService);
        this.ruleExecutor = ruleExecutor;
        this.kafkaAdminService = kafkaAdminService;
    }

    public void createProcessGeneric(ProcessConsumer processConsumer) {
        log.info("Create topic for importer");
        kafkaAdminService.buildTopic(SIMULATE_OUTPUT);
        log.info("Create process importer {}", processConsumer.getName());
        List<GenericFilter> genericFilters = new ArrayList<>();
        for (ProcessFilter processFilter : processConsumer.getProcessFilter()) {
            genericFilters.add(ruleExecutor.instanciate(processFilter.getName(), processFilter.getCriteria()));
        }
        SimulateStreamService simulateStreamService = new SimulateStreamService(
                getGenericValidator(),
                getGenericTransformator(),
                getGenericParser(),
                processConsumer,
                genericFilters);
        getListConsumer().add(simulateStreamService);
        getExecutor().submit(simulateStreamService);
    }

}
