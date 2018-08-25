package io.skalogs.skaetl.web;

/*-
 * #%L
 * generator
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.skalogs.skaetl.generator.*;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private final GeneratorSecu generatorSecu;

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
    @GetMapping("/inputCart")
    public void inputCart(@RequestParam("nbCustomer") Integer nbCustomer,
                          @RequestParam("nbShowByMinute") Integer nbShowByMinute,
                          @RequestParam("nbAddToCartByMinute") Integer nbAddToCartByMinute,
                          @RequestParam("nbPaySuccessByMinute") Integer nbPaySuccessByMinute,
                          @RequestParam("nbPayNotSuccessByMinute") Integer nbPayNotSuccessByMinute,
                          @RequestParam("timeToGenerateInMinute") Integer timeToGenerateInMinute) {
        generatorCartService.generateData(nbCustomer, nbShowByMinute, nbAddToCartByMinute, nbPaySuccessByMinute, nbPayNotSuccessByMinute, timeToGenerateInMinute);
    }

    @ResponseStatus(CREATED)
    @GetMapping("/inputSpecificCart")
    public void inputSpecificCart(@RequestParam("nbCustomer") Integer nbCustomer) {
        generatorCartService.generateSpecificUsecase(nbCustomer);
    }

    @ResponseStatus(CREATED)
    @GetMapping("/inputCredit")
    public void inputCredit(@RequestParam("timeToGenerateInMinute") Integer timeToGenerateInMinute,
                            @RequestParam("nbView") Integer nbView,
                            @RequestParam("nbCredit") Integer nbCredit,
                            @RequestParam("nbRandomRq") Integer nbRandomRq) {
        generatorCreditService.generateData(timeToGenerateInMinute, nbView, nbCredit, nbRandomRq);
    }

    @ResponseStatus(CREATED)
    @GetMapping("/inputCreditLong")
    public void inputCreditLong(@RequestParam("timeToGenerateInMinute") Integer timeToGenerateInMinute,
                                @RequestParam("nbView") Integer nbView,
                                @RequestParam("nbCredit") Integer nbCredit,
                                @RequestParam("nbRandomRq") Integer nbRandomRq) {
        generatorCreditService.generateLongData(timeToGenerateInMinute, nbView, nbCredit, nbRandomRq);
    }


    @ResponseStatus(CREATED)
    @GetMapping("/inputCreditRef")
    public void inputCreditRef(@RequestParam("timeToGenerateInMinute") Integer timeToGenerateInMinute,
                               @RequestParam("nbCredit") Integer nbCredit) {
        generatorCreditService.generateDataForRef(timeToGenerateInMinute, nbCredit);
    }

    @ResponseStatus(CREATED)
    @GetMapping("/inputSecu")
    public void inputSecu(@RequestParam("timeToGenerateInMinute") Integer timeToGenerateInMinute,
                          @RequestParam("firewall") Boolean firewall,
                          @RequestParam("proxy") Boolean proxy,
                          @RequestParam("proxy") Boolean database,
                          @RequestParam("connexion") Boolean connexion,
                          @RequestParam("nbUser") Integer nbUser) {
        generatorSecu.generateLongData(timeToGenerateInMinute, firewall, proxy, connexion, database, nbUser);
    }


}
