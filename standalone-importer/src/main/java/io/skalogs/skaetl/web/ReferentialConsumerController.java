package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.service.ReferentialImporter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/referentialConsumer")
@AllArgsConstructor
public class ReferentialConsumerController {

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
}
