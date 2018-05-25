package io.skalogs.skaetl.web;

import io.skalogs.skaetl.generator.*;
import io.skalogs.skaetl.web.domain.PayloadTopic;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/generator")
@AllArgsConstructor
public class GeneratorController {

    private final GeneratorCartService generatorCartService;
    private final GeneratorCreditService generatorCreditService;
    private final GeneratorService generatorService;
    private final GeneratorErrorService generatorErrorService;
    private final GeneratorRetryService generatorRetryService;

    @ResponseStatus(CREATED)
    @PostMapping("/inputTopic")
    public void inputTopic(@Valid @RequestBody PayloadTopic payload) {
        generatorService.createRandom(payload.getNbElemBySlot(), payload.getNbSlot());
    }

    @ResponseStatus(CREATED)
    @PostMapping("/inputTopicNetwork")
    public void inputTopicNetwork(@Valid @RequestBody PayloadTopic payload) {
        generatorService.createRandomNetwork(payload.getNbElemBySlot());
    }

    @ResponseStatus(CREATED)
    @PostMapping(value = "/inputTopicApacheAsJSON", produces = MediaType.APPLICATION_JSON_VALUE)
    public void inputTopicApacheAsJSON(@Valid @RequestBody PayloadTopic payload) {
        generatorService.createApacheAsJSON(payload.getNbElemBySlot(), payload.getNbSlot());
    }

    @ResponseStatus(CREATED)
    @PostMapping(value = "/inputTopicApacheAsTEXT", produces = MediaType.TEXT_PLAIN_VALUE)
    public void inputTopicApacheAsTEXT(@Valid @RequestBody PayloadTopic payload) {
        generatorService.createApacheAsText(payload.getNbElemBySlot(), payload.getNbSlot());
    }

    @ResponseStatus(CREATED)
    @PostMapping("/inputErrorTopic")
    public void inputErrorTopic(@Valid @RequestBody PayloadTopic payload) {
        generatorErrorService.createRandom(payload.getNbElemBySlot());
    }

    @ResponseStatus(CREATED)
    @PostMapping("/inputRetryTopic")
    public void inputRetryTopic(@Valid @RequestBody PayloadTopic payload) {
        generatorRetryService.createRandom(payload.getNbElemBySlot(), payload.getNbSlot());
    }

    @ResponseStatus(CREATED)
    @GetMapping("/inputCard")
    public void inputCard(@PathParam("nbCustomer") Integer nbCustomer,
                          @PathParam("nbShowByMinute") Integer nbShowByMinute,
                          @PathParam("nbAddToCardByMinute") Integer nbAddToCardByMinute,
                          @PathParam("nbPaySuccessByMinute") Integer nbPaySuccessByMinute,
                          @PathParam("nbPayNotSuccessByMinute") Integer nbPayNotSuccessByMinute,
                          @PathParam("timeToGenerateInMinute") Integer timeToGenerateInMinute ) {
        generatorCartService.generateData(nbCustomer, nbShowByMinute, nbAddToCardByMinute, nbPaySuccessByMinute, nbPayNotSuccessByMinute, timeToGenerateInMinute);
    }

    @ResponseStatus(CREATED)
    @GetMapping("/inputSpecificCard")
    public void inputSpecificCard(@PathParam("nbCustomer") Integer nbCustomer) {
        generatorCartService.generateSpecificUsecase(nbCustomer);
    }

    @ResponseStatus(CREATED)
    @GetMapping("/inputCredit")
    public void inputCredit(@PathParam("timeToGenerateInMinute") Integer timeToGenerateInMinute,
                            @PathParam("nbView") Integer nbView,
                            @PathParam("nbCredit") Integer nbCredit,
                            @PathParam("nbRandomRq") Integer nbRandomRq) {
        generatorCreditService.generateData(timeToGenerateInMinute,nbView,nbCredit,nbRandomRq);
    }

}
