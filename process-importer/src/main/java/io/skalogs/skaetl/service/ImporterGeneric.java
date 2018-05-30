package io.skalogs.skaetl.service;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.domain.ProcessConstants;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.ProcessFilter;
import io.skalogs.skaetl.rules.filters.GenericFilter;
import io.skalogs.skaetl.rules.filters.RuleFilterExecutor;
import io.skalogs.skaetl.service.processor.JsonNodeToElasticSearchProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Lazy(value = false)
@Slf4j
public class ImporterGeneric extends AbstractGenericImporter {

    private final RuleFilterExecutor ruleFilterExecutor;
    private final ESErrorRetryWriter esErrorRetryWriter;
    private final KafkaAdminService kafkaAdminService;
    private final ApplicationContext applicationContext;
    private final EmailService emailService;
    private final SnmpService snmpService;

    @PostConstruct
    public void init() {
        sendToRegistry("addService");
    }

    public ImporterGeneric(ESErrorRetryWriter esErrorRetryWriter, GenericValidator genericValidator, GenericTransformator transformValidator, GenericParser genericParser, GenericFilterService genericFilterService, RuleFilterExecutor ruleFilterExecutor, KafkaAdminService kafkaAdminService, ProcessConfiguration processConfiguration, ExternalHTTPService externalHTTPService, ApplicationContext applicationContext, EmailService emailService, SnmpService snmpService) {
        super(genericValidator, transformValidator, genericParser, genericFilterService, processConfiguration, externalHTTPService);
        this.ruleFilterExecutor = ruleFilterExecutor;
        this.esErrorRetryWriter = esErrorRetryWriter;
        this.kafkaAdminService = kafkaAdminService;
        this.applicationContext = applicationContext;
        this.emailService = emailService;
        this.snmpService = snmpService;
    }

    public void createProcessGeneric(ProcessConsumer processConsumer) {

        processConsumer.setTimestamp(new Date());
        log.info("Create topic for importer");
        kafkaAdminService.buildTopic(processConsumer.getProcessInput().getTopicInput(),
                processConsumer.getIdProcess() + ProcessConstants.TOPIC_PARSED_PROCESS,
                processConsumer.getIdProcess() + ProcessConstants.TOPIC_TREAT_PROCESS
        );
        processConsumer.getProcessOutput().stream()
                .forEach(processOutput -> kafkaAdminService.buildTopic(processOutput.getParameterOutput().getTopicOut()));

        processConsumer.getProcessParser().stream()
                .forEach(processParser -> kafkaAdminService.buildTopic(processParser.getFailForwardTopic()));
        processConsumer.getProcessFilter().stream()
                .forEach(processFilter -> kafkaAdminService.buildTopic(processFilter.getFailForwardTopic()));
        getExternalHTTPService().buildCache(processConsumer);
        log.info("Create process importer {}", processConsumer.getName());
        List<GenericFilter> genericFilters = new ArrayList<>();
        for (ProcessFilter processFilter : processConsumer.getProcessFilter()) {
            genericFilters.add(ruleFilterExecutor.instanciate(processFilter.getName(), processFilter.getCriteria(), processFilter));
        }
        ProcessStreamService processStreamService = new ProcessStreamService(
                getGenericValidator(),
                getGenericTransformator(),
                getGenericParser(),
                getGenericFilterService(),
                processConsumer,
                genericFilters,
                esErrorRetryWriter,
                applicationContext.getBean(JsonNodeToElasticSearchProcessor.class),
                emailService,
                snmpService
        );
        getListConsumer().add(processStreamService);
        getExecutor().submit(processStreamService);
        sendToRegistry("refresh");
    }

    @Scheduled(initialDelay = 20 * 1000, fixedRate = 1 * 60 * 1000)
    public void refresh() {
        sendToRegistry("refresh");
    }
}
