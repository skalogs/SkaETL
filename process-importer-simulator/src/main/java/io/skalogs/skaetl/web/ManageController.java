package io.skalogs.skaetl.web;

/*-
 * #%L
 * process-importer-simulator
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
import io.skalogs.skaetl.service.SimulateImporter;
import io.skalogs.skaetl.service.SimulateResultService;
import io.skalogs.skaetl.service.SimulateTextService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class ManageController {

    private final SimulateImporter simulateImporter;
    private final SimulateResultService simulateResultService;
    private final SimulateTextService simulateTextService;

    @ResponseStatus(CREATED)
    @PostMapping("/active")
    public void simulate(@RequestBody ProcessConsumer processConsumer) {
        simulateImporter.createProcessGeneric(processConsumer);
    }

    @GetMapping("/status")
    public List<StatusConsumer> status() {
        return simulateImporter.statusExecutor();
    }

    @PostMapping("/readOutput")
    public List<SimulateData> readOutput(@RequestBody PayloadReadOutput payloadReadOutput) {
        return simulateResultService.readOutPut(payloadReadOutput.getBootStrapServers(), payloadReadOutput.getMaxRecords(), payloadReadOutput.getWindowTime());
    }

    @PostMapping("/readOutputFromText")
    public SimulateData readOutputFromText(@RequestBody PayloadTextForReadOutput payloadTextForReadOutput) {
        return simulateTextService.readOutputFromText(payloadTextForReadOutput.getTextSubmit(), payloadTextForReadOutput.getProcessConsumer());
    }

    @PostMapping("/captureRawData")
    public List<String> captureRawData(@RequestBody PayloadReadOutput payloadReadOutput) {
        return simulateResultService.readKafkaRawData(payloadReadOutput.getBootStrapServers(), payloadReadOutput.getTopic(), payloadReadOutput.getMaxRecords(), payloadReadOutput.getWindowTime(),payloadReadOutput.getOffset(),payloadReadOutput.getDeserializer());
    }
}
