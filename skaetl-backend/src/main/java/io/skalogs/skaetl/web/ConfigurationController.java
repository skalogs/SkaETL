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

import io.skalogs.skaetl.domain.ConfigurationLogstash;
import io.skalogs.skaetl.service.ConfService;
import io.skalogs.skaetl.web.domain.ConfLogstashWeb;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/configuration")
@AllArgsConstructor
public class ConfigurationController {

    private final ConfService confService;

    @ResponseStatus(OK)
    @PostMapping("createConfiguration")
    public void createConfiguration(@RequestBody ConfigurationLogstash configurationLogstash) {
        confService.createConfiguration(configurationLogstash);
    }

    @ResponseStatus(OK)
    @GetMapping("generate")
    public ConfLogstashWeb generate(@PathParam("idConfiguration") String idConfiguration) {
        return confService.generate(idConfiguration);
    }

    @ResponseStatus(OK)
    @PostMapping("editConfiguration")
    public void editConfiguration(@RequestBody ConfigurationLogstash configurationLogstash) {
        confService.editConfiguration(configurationLogstash);
    }

    @ResponseStatus(OK)
    @GetMapping("findAll")
    public List<ConfigurationLogstash> findAll() {
        return confService.findAll();
    }

    @ResponseStatus(OK)
    @GetMapping("getConfiguration")
    public ConfigurationLogstash getConfiguration(@PathParam("idConfiguration") String idConfiguration) {
        return confService.getConfiguration(idConfiguration);
    }

    @ResponseStatus(OK)
    @GetMapping("deleteConfiguration")
    public List<ConfigurationLogstash> deleteConfiguration(@PathParam("idConfiguration") String idConfiguration) {
        confService.deleteConfiguration(idConfiguration);
        return findAll();
    }

    @ResponseStatus(OK)
    @GetMapping("activeConfiguration")
    public List<ConfigurationLogstash> activeConfiguration(@PathParam("idConfiguration") String idConfiguration) {
        confService.activeConfiguration(idConfiguration);
        return findAll();
    }

    @ResponseStatus(OK)
    @GetMapping("deactiveConfiguration")
    public List<ConfigurationLogstash> deactiveConfiguration(@PathParam("idConfiguration") String idConfiguration) {
        confService.deactiveConfiguration(idConfiguration);
        return findAll();
    }

}
