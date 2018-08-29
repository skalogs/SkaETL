package io.skalogs.skaetl.web;

/*-
 * #%L
 * skaetl-backend
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

import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.repository.ParserDescriptionRepository;
import io.skalogs.skaetl.repository.TransformatorDescriptionRepository;
import io.skalogs.skaetl.repository.ValidatorDescriptionRepository;
import io.skalogs.skaetl.service.ProcessService;
import io.skalogs.skaetl.service.UtilsNetworkService;
import io.skalogs.skaetl.web.domain.NetworkWeb;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/process")
@AllArgsConstructor
public class ProcessController {

    private final ProcessService processService;
    private final UtilsNetworkService utilsNetworkService;
    private final ParserDescriptionRepository parserDescriptionRepository;
    private final TransformatorDescriptionRepository transformatorDescriptionRepository;
    private final ValidatorDescriptionRepository validatorDescriptionRepository;

    @ResponseStatus(OK)
    @GetMapping("network")
    public NetworkWeb viewNetwork() {
       return utilsNetworkService.viewNetwork();
    }

    @GetMapping("initDefaults")
    public ProcessConsumer init() {
        return processService.initProcessConsumer();
    }

    @ResponseStatus(OK)
    @PostMapping("save")
    public void save(@RequestBody ProcessConsumer processConsumer) {
        processService.saveOrUpdate(processConsumer);
    }

    @ResponseStatus(OK)
    @GetMapping("findAll")
    public List<ConsumerState> findAll() {
        return processService.findAll();
    }

    @ResponseStatus(OK)
    @GetMapping("findProcess")
    public ProcessConsumer findProcess(@PathParam("idProcess") String idProcess) {
        return processService.findProcess(idProcess);
    }

    @ResponseStatus(OK)
    @DeleteMapping("deleteProcess")
    public void deleteProcess(@RequestParam("idProcess") String id) {
        processService.deleteProcess(id);
    }


    @ResponseStatus(OK)
    @GetMapping("findConsumerState")
    public ConsumerState findConsumerState(@PathParam("idProcess") String idProcess) {
        return processService.findConsumerState(idProcess);
    }

    @ResponseStatus(OK)
    @GetMapping("activate")
    public void activate(@PathParam("idProcess") String idProcess) throws Exception {
        processService.activateProcess(processService.findProcess(idProcess));
    }

    @ResponseStatus(OK)
    @GetMapping("deactivate")
    public void deactivate(@PathParam("idProcess") String idProcess) throws Exception {
        processService.deactivateProcess(processService.findProcess(idProcess));
    }

    @ResponseStatus(OK)
    @GetMapping("scaleup")
    public void scaleup(@PathParam("idProcess") String idProcess) throws Exception {
        processService.scaleup(idProcess);
    }

    @ResponseStatus(OK)
    @GetMapping("scaledown")
    public void scaledown(@PathParam("idProcess") String idProcess) throws Exception {
        processService.scaledown(idProcess);
    }

    @GetMapping("parsers")
    public List<ParserDescription> parsers() {
        return parserDescriptionRepository.findAll();
    }

    @GetMapping("transformators")
    public List<TransformatorDescription> transformators() {
        return transformatorDescriptionRepository.findAll();
    }

    @GetMapping("validators")
    public List<ValidatorDescription> validators() {
        return validatorDescriptionRepository.findAll();
    }
}
