package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.StatusConsumer;
import io.skalogs.skaetl.service.ImporterGeneric;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class ManageController {

    private final ImporterGeneric importer;

    @ResponseStatus(CREATED)
    @PostMapping("/activate")
    public void activate(@RequestBody ProcessConsumer processConsumer) {
        importer.activate(processConsumer);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/deactivate")
    public void disable(@RequestBody ProcessConsumer processConsumer) {
        importer.disable(processConsumer);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/disableAll")
    public void disableAll() {
        importer.disableAll();
    }

    @GetMapping("/status")
    public List<StatusConsumer> status() {
        return importer.statusExecutor();
    }
}
