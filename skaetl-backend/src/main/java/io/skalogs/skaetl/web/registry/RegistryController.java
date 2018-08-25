package io.skalogs.skaetl.web.registry;

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
