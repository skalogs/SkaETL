package io.skalogs.skaetl.web.registry;


import io.skalogs.skaetl.domain.RegistryWorker;
import io.skalogs.skaetl.service.RegistryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/process/registry")
public class RegistryController {

    private RegistryService registryService;

    @ResponseStatus(OK)
    @PostMapping("/addService")
    public void addService(@RequestBody RegistryWorker registryWorker) {
        registryService.addHost(registryWorker);
    }


    @ResponseStatus(OK)
    @PostMapping("/refresh")
    public void refresh(@RequestBody RegistryWorker registryWorker) {
        registryService.refresh(registryWorker);
    }

    @GetMapping("/all-patterns")
    public List<RegistryWorker> all() {
        return registryService.getAllStatus();
    }

}
