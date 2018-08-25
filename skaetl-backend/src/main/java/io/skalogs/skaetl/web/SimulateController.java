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

import io.skalogs.skaetl.domain.PayloadReadOutput;
import io.skalogs.skaetl.domain.PayloadTextForReadOutput;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.SimulateData;
import io.skalogs.skaetl.service.ImporterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/simulate")
@AllArgsConstructor
public class SimulateController {

    private final ImporterService importerService;

    @ResponseStatus(OK)
    @PostMapping("launchSimulate")
    public void launchSimulate(@RequestBody ProcessConsumer processConsumer) {
        importerService.launchSimulate(processConsumer);
    }

    @ResponseStatus(OK)
    @PostMapping("capture")
    public List<SimulateData> capture(@RequestBody PayloadReadOutput payloadReadOutput) {
        return importerService.capture(payloadReadOutput);
    }

    @ResponseStatus(OK)
    @PostMapping("captureFromText")
    public SimulateData captureFromText(@RequestBody PayloadTextForReadOutput payloadTextForReadOutput) {
        return importerService.captureFromText(payloadTextForReadOutput);
    }

    @ResponseStatus(OK)
    @PostMapping("raw/captureRaw")
    public List<String> captureRaw(@RequestBody PayloadReadOutput payloadReadOutput) {
        return importerService.captureRawData(payloadReadOutput);
    }


}
