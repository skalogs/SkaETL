package io.skalogs.skaetl.service;

/*-
 * #%L
 * process-importer-impl
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

import io.skalogs.skaetl.domain.ProcessConsumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
@Getter
@AllArgsConstructor
public abstract class AbstractStreamProcess implements Runnable {

    private final GenericValidator genericValidator;
    private final GenericTransformator genericTransformator;
    private final GenericParser genericParser;
    private final GenericFilterService genericFilterService;
    private final ProcessConsumer processConsumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final List<KafkaStreams> streams = new ArrayList<>();

    public abstract void createStreamProcess();

    public void addStreams(KafkaStreams streams) {
        this.streams.add(streams);
    }

    public void shutdownAllStreams() {
        streams.stream()
                .forEach(e -> e.close());
    }

    @Override
    public void run() {
        createStreamProcess();
    }


    public String getBootstrapServer() {
        return getProcessConsumer().getProcessInput().getHost() + ":" + getProcessConsumer().getProcessInput().getPort();
    }

}
