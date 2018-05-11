package io.skalogs.skaetl.web;

import io.skalogs.skaetl.domain.ConsumerState;
import io.skalogs.skaetl.domain.ProcessDefinition;
import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.service.ReferentialService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/referential")
@AllArgsConstructor
public class ReferentialController {

    private final ReferentialService referentialService;

    @ResponseStatus(OK)
    @GetMapping("findAll")
    public List<ConsumerState> findAll() {
        return referentialService.findAll();
    }

    @ResponseStatus(OK)
    @GetMapping("find")
    public ProcessDefinition find(@PathParam("idReferential") String idReferential) {
        return referentialService.findReferential(idReferential);
    }

    @ResponseStatus(OK)
    @GetMapping("delete")
    public List<ConsumerState> delete(@PathParam("idReferential") String idReferential) {
        referentialService.deleteReferential(idReferential);
        return findAll();
    }

    @GetMapping("init")
    public ProcessReferential init() {
        return referentialService.init();
    }

    @ResponseStatus(OK)
    @PostMapping("update")
    public List<ConsumerState> update(@RequestBody ProcessReferential processReferential) {
        referentialService.updateReferential(processReferential);
        return findAll();
    }

    @ResponseStatus(OK)
    @GetMapping("activate")
    public void activate(@PathParam("idReferential") String idReferential) throws Exception {
        referentialService.activateProcess((ProcessReferential) referentialService.findReferential(idReferential));
    }

    @ResponseStatus(OK)
    @GetMapping("deactivate")
    public void deactivate(@PathParam("idReferential") String idReferential) throws Exception {
        referentialService.deactivateProcess((ProcessReferential) referentialService.findReferential(idReferential));
    }

}
