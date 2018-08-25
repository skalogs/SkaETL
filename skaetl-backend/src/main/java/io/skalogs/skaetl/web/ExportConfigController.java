package io.skalogs.skaetl.web;

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
