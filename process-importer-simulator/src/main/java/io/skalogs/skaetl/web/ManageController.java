package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.SimulateImporter;
import io.skalogs.skaetl.service.SimulateResultService;
import io.skalogs.skaetl.service.SimulateTextService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class ManageController {

    private final SimulateImporter simulateImporter;
    private final SimulateResultService simulateResultService;
    private final SimulateTextService simulateTextService;

    @ResponseStatus(CREATED)
    @PostMapping("/active")
    public void simulate(@RequestBody ProcessConsumer processConsumer) {
        simulateImporter.createProcessGeneric(processConsumer);
    }

    @GetMapping("/status")
    public List<StatusConsumer> status() {
        return simulateImporter.statusExecutor();
    }

    @PostMapping("/readOutput")
    public List<SimulateData> readOutput(@RequestBody PayloadReadOutput payloadReadOutput) {
        return simulateResultService.readOutPut(payloadReadOutput.getBootStrapServers(), payloadReadOutput.getMaxRecords(), payloadReadOutput.getWindowTime());
    }

    @PostMapping("/readOutputFromText")
    public SimulateData readOutputFromText(@RequestBody PayloadTextForReadOutput payloadTextForReadOutput) {
        return simulateTextService.readOutputFromText(payloadTextForReadOutput.getTextSubmit(), payloadTextForReadOutput.getProcessConsumer());
    }

    @PostMapping("/captureRawData")
    public List<String> captureRawData(@RequestBody PayloadReadOutput payloadReadOutput) {
        return simulateResultService.readKafkaRawData(payloadReadOutput.getBootStrapServers(), payloadReadOutput.getTopic(), payloadReadOutput.getMaxRecords(), payloadReadOutput.getWindowTime(),payloadReadOutput.getOffset(),payloadReadOutput.getDeserializer());
    }
}
