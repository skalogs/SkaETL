package io.skalogs.skaetl.service;

/*-
 * #%L
 * retry-importer
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
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.serdes.ValidateDataDeserializer;
import io.skalogs.skaetl.serdes.ValidateDataSerializer;
import io.skalogs.skaetl.service.processor.ValidateDataToElasticSearchProcessor;
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
public class RetryImporter {

    private static final String INPUT_PROCESS_RETRY = "es-retry";
    private final  ValidateDataToElasticSearchProcessor elasticSearchProcessor;
    private final KafkaConfiguration kafkaConfiguration;
    private KafkaStreams retryStream;

    public RetryImporter(ValidateDataToElasticSearchProcessor elasticSearchProcessor, KafkaConfiguration kafkaConfiguration, KafkaAdminService kafkaAdminService, ProcessConfiguration processConfiguration) {
        this.elasticSearchProcessor = elasticSearchProcessor;
        this.kafkaConfiguration = kafkaConfiguration;
        kafkaAdminService.buildTopic(kafkaConfiguration.getRetryTopic());
        if (processConfiguration.isActive()) {
            activate();
        }
    }

    public void activate() {
        log.info("Activating retry importer");
        StreamsBuilder builder = new StreamsBuilder();
        final Serde<ValidateData> validateDataSerdes = Serdes.serdeFrom(new ValidateDataSerializer(), new ValidateDataDeserializer());

        KStream<String, ValidateData> streamToES = builder.stream(kafkaConfiguration.getRetryTopic(), Consumed.with(Serdes.String(), validateDataSerdes));
        streamToES.process(() -> elasticSearchProcessor);

        retryStream = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties(INPUT_PROCESS_RETRY, kafkaConfiguration.getBootstrapServers()));
        Runtime.getRuntime().addShutdownHook(new Thread(retryStream::close));
        retryStream.start();
    }

    public void deactivate() {
        log.info("Deactivating retry importer");
        retryStream.close();
    }
}
