package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ConsumerState;
import io.skalogs.skaetl.domain.ProcessMetric;
import io.skalogs.skaetl.service.MetricProcessService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/metric")
@AllArgsConstructor
public class MetricProcessController {

    private final MetricProcessService metricProcessService;

    @ResponseStatus(OK)
    @GetMapping("listProcess")
    public List<ConsumerState> list() {
        return metricProcessService.findAll();
    }

    @GetMapping("findById")
    public ProcessMetric findById(@RequestParam("idProcess") String id) {
        return (ProcessMetric) metricProcessService.findById(id);
    }

    @ResponseStatus(OK)
    @GetMapping("init")
    public ProcessMetric init() {
        return metricProcessService.init();
    }

    @ResponseStatus(OK)
    @PostMapping("update")
    public void update(@RequestBody ProcessMetric processMetric) {
        metricProcessService.updateProcess(processMetric);
    }

    @ResponseStatus(OK)
    @GetMapping("activate")
    public void activate(@RequestParam("idProcess") String id) {
        metricProcessService.activateProcess(findById(id));
    }

    @ResponseStatus(OK)
    @GetMapping("deactivate")
    public void deactivate(@RequestParam("idProcess") String id) {
        metricProcessService.deactivateProcess(findById(id));
    }

    @ResponseStatus(OK)
    @DeleteMapping("delete")
    public void delete(@RequestParam("idProcess") String id) {
        metricProcessService.deleteProcess(findById(id));
    }
}
