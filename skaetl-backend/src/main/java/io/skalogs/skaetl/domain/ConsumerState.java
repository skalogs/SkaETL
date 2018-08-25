package io.skalogs.skaetl.domain;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@Wither
public class ConsumerState {
    private final String id;
    private final Integer nbInstance;
    private final WorkerType workerType;
    private final Set<String> registryWorkers;
    private final StatusProcess statusProcess;
    private final ProcessDefinition processDefinition;

    public ConsumerState(ProcessDefinition processDefinition, WorkerType workerType, StatusProcess statusProcess) {
        this.id = processDefinition.getIdProcess();
        this.processDefinition = processDefinition;
        this.statusProcess = statusProcess;
        this.workerType = workerType;
        this.nbInstance = 1;
        this.registryWorkers = new HashSet<>();
    }
}
