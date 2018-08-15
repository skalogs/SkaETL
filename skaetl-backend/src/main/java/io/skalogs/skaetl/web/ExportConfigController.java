package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.ProcessDefinition;
import io.skalogs.skaetl.domain.ProcessMetric;
import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.service.MetricProcessService;
import io.skalogs.skaetl.service.ProcessService;
import io.skalogs.skaetl.service.ReferentialService;
import io.skalogs.skaetl.service.RegistryService;
import io.skalogs.skaetl.web.domain.SkalogsExport;
import lombok.AllArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class ExportConfigController {
    private final RegistryService registryService;
    private final ProcessService processService;
    private final MetricProcessService metricProcessService;
    private final ReferentialService referentialService;


    private final BuildProperties buildProperties;

    @GetMapping("/export/all")
    public SkalogsExport export() {

        List<ProcessDefinition> processDefinitions = registryService.findAll()
                .stream()
                .map(e -> e.getProcessDefinition())
                .collect(Collectors.toList());
        return SkalogsExport.builder()
                .version(buildProperties.getVersion())
                .processDefinitions(processDefinitions)
                .build();
    }


    @ResponseStatus(CREATED)
    @PostMapping("/import/all")
    public void importAll(@RequestBody SkalogsExport skalogsExport) {
        skalogsExport.getProcessDefinitions()
                .stream()
                .filter(definition -> definition instanceof ProcessConsumer)
                .map(processDefinition -> (ProcessConsumer)processDefinition)
                .forEach(processConsumer  -> processService.saveOrUpdate(processConsumer));
        skalogsExport.getProcessDefinitions()
                .stream()
                .filter(definition -> definition instanceof ProcessMetric)
                .map(processDefinition -> (ProcessMetric)processDefinition)
                .forEach(processMetric -> metricProcessService.updateProcess(processMetric));
        skalogsExport.getProcessDefinitions()
                .stream()
                .filter(definition -> definition instanceof ProcessReferential)
                .map(processDefinition -> (ProcessReferential)processDefinition)
                .forEach(processReferential -> referentialService.updateReferential(processReferential));

    }
}
