package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ProcessMetric;
import io.skalogs.skaetl.service.MetricImporter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/metricConsumer")
@AllArgsConstructor
public class MetricConsumerController {

    private final MetricImporter metricImporter;

    @ResponseStatus(CREATED)
    @PostMapping("/activate")
    public void createProcessGeneric(@RequestBody ProcessMetric processMetric) {
        metricImporter.activate(processMetric);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/deactivate")
    public void disable(@RequestBody ProcessMetric processMetric) {
        metricImporter.deactivate(processMetric);
    }


}
