package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ProcessMetric;
import io.skalogs.skaetl.service.MetricImporter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class ManageController {

    private final MetricImporter metricImporter;

    @ResponseStatus(CREATED)
    @PostMapping("/activate")
    public void activate(@RequestBody ProcessMetric processMetric) {
        metricImporter.activate(processMetric);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/deactivate")
    public void deactivate(@RequestBody ProcessMetric processMetric) {
        metricImporter.deactivate(processMetric);
    }


}
