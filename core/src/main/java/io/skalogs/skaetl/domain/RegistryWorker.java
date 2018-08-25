package io.skalogs.skaetl.domain;

/*-
 * #%L
 * core
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


import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegistryWorker {
    private String ip;
    private String port;
    private String name;
    private StatusWorker status;
    private Date dateRefresh;
    private WorkerType workerType;
    @Builder.Default
    private List<StatusConsumer> statusConsumerList = new ArrayList<>();

    public String getBaseUrl() {
        return "http://" + ip + ":" + port;
    }

    public boolean hasRunning(String id) {
        return statusConsumerList
                .stream()
                .filter(consumer -> consumer.getStatusProcess() == StatusProcess.ENABLE)
                .anyMatch(consumer -> consumer.getIdProcessConsumer().equals(id));
    }

    public String getFQDN() {
        return name + "-" + workerType.name();
    }
}
