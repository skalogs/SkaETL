package io.skalogs.skaetl.web;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.ImporterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/simulate")
@AllArgsConstructor
public class SimulateController {

    private final ImporterService importerService;

    @ResponseStatus(OK)
    @PostMapping("launchSimulate")
    public void launchSimulate(@RequestBody ProcessConsumer processConsumer) {
        importerService.launchSimulate(processConsumer);
    }

    @ResponseStatus(OK)
    @PostMapping("capture")
    public List<SimulateData> capture(@RequestBody PayloadReadOutput payloadReadOutput) {
        return importerService.capture(payloadReadOutput);
    }

    @ResponseStatus(OK)
    @PostMapping("captureFromText")
    public SimulateData captureFromText(@RequestBody PayloadTextForReadOutput payloadTextForReadOutput) {
        return importerService.captureFromText(payloadTextForReadOutput);
    }

    @ResponseStatus(OK)
    @PostMapping("raw/captureInput")
    public List<String> captureInput(@RequestBody PayloadIdProcess payloadIdProcess) {
        return importerService.captureInput(payloadIdProcess);
    }

    @ResponseStatus(OK)
    @PostMapping("raw/captureTransformation")
    public List<JsonNode> captureTransformation(@RequestBody PayloadIdProcess payloadIdProcess) {
        return importerService.captureTransformation(payloadIdProcess);
    }

    @ResponseStatus(OK)
    @PostMapping("raw/captureRaw")
    public List<String> captureRaw(@RequestBody PayloadReadOutput payloadReadOutput) {
        return importerService.captureRawData(payloadReadOutput);
    }


}
