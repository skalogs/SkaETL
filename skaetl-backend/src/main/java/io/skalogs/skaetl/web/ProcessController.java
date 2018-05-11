package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ConsumerState;
import io.skalogs.skaetl.domain.ProcessConsumer;
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

    @ResponseStatus(OK)
    @GetMapping("network")
    public NetworkWeb viewNetwork() {
       return utilsNetworkService.viewNetwork();
    }

    @GetMapping("init")
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
}
