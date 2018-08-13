package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.service.ImporterGeneric;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/processConsumer")
@AllArgsConstructor
public class ProcessConsumerController {

    private final ImporterGeneric importer;

    @ResponseStatus(CREATED)
    @PostMapping("/activate")
    public void createProcessGeneric(@RequestBody ProcessConsumer processConsumer) {
        importer.activate(processConsumer);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/deactivate")
    public void disable(@RequestBody ProcessConsumer processConsumer) {
        importer.disable(processConsumer);
    }


}
