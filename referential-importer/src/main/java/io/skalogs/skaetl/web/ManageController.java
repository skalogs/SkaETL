package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.domain.StatusConsumer;
import io.skalogs.skaetl.service.ReferentialImporter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
@Slf4j
public class ManageController {

    private final ReferentialImporter referentialImporter;

    @ResponseStatus(CREATED)
    @PostMapping("/activate")
    public void createProcessGeneric(@RequestBody ProcessReferential processReferential) {
        referentialImporter.activate(processReferential);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/deactivate")
    public void disable(@RequestBody ProcessReferential processReferential) {
        referentialImporter.deactivate(processReferential);
    }

    @GetMapping("/status")
    public List<StatusConsumer> status() {
        return referentialImporter.statusExecutor();
    }

}
