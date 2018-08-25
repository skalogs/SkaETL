package io.skalogs.skaetl.web;

/*-
 * #%L
 * referential-importer
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

import io.skalogs.skaetl.domain.ProcessReferential;
import io.skalogs.skaetl.domain.StatusConsumer;
import io.skalogs.skaetl.service.ReferentialImporter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
@Slf4j
public class ManageController {

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

    @GetMapping("/status")
    public List<StatusConsumer> status() {
        return referentialImporter.statusExecutor();
    }

    @GetMapping("/flush")
    public void flush() {
        referentialImporter.flush();
    }

}
