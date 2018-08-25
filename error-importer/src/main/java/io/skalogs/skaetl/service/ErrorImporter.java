package io.skalogs.skaetl.service;

/*-
 * #%L
 * error-importer
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

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.config.ProcessConfiguration;
import io.skalogs.skaetl.domain.ErrorData;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
@Slf4j
public class ErrorImporter {


    private static final String INPUT_PROCESS_ERROR = "es-error";
    private final ErrorToElasticsearchProcessor elasticsearchProcessor;
    private final KafkaConfiguration kafkaConfiguration;
    private KafkaStreams errorStream;

    public ErrorImporter(ErrorToElasticsearchProcessor elasticsearchProcessor, KafkaConfiguration kafkaConfiguration, KafkaAdminService kafkaAdminService, ProcessConfiguration processConfiguration) {
        kafkaAdminService.buildTopic(kafkaConfiguration.getErrorTopic());
        this.elasticsearchProcessor = elasticsearchProcessor;
        this.kafkaConfiguration = kafkaConfiguration;
        if (processConfiguration.isActive()) {
            activate();
        }
    }

    public void activate() {
        log.info("Activating error importer");
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<ErrorData> errorDataSerde = Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(ErrorData.class));

        KStream<String, ErrorData> streamToES = builder.stream(kafkaConfiguration.getErrorTopic(), Consumed.with(Serdes.String(), errorDataSerde));

        streamToES.process(() -> elasticsearchProcessor);

        errorStream = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties(INPUT_PROCESS_ERROR, kafkaConfiguration.getBootstrapServers()));
        Runtime.getRuntime().addShutdownHook(new Thread(errorStream::close));

        errorStream.start();
    }

    public void deactivate() {
        log.info("Deactivating error importer");
        errorStream.close();
    }
}
