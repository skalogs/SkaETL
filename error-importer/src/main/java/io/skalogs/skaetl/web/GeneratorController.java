package io.skalogs.skaetl.web;

import io.skalogs.skaetl.generator.GeneratorErrorService;
import io.skalogs.skaetl.web.domain.PayloadTopic;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/generator")
@AllArgsConstructor
public class GeneratorController {

    private final GeneratorErrorService generatorErrorService;

    @ResponseStatus(CREATED)
    @PutMapping("/errorData")
    public void errorData(@Valid @RequestBody PayloadTopic payload) {
        generatorErrorService.createRandom(payload.getNbElemBySlot());
    }


}
