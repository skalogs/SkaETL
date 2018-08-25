package io.skalogs.skaetl.service;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.ErrorData;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.serdes.ErrorDataSerializer;
import io.skalogs.skaetl.serdes.JsonNodeSerialializer;
import io.skalogs.skaetl.utils.JSONUtils;
import io.skalogs.skaetl.utils.KafkaUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ESErrorRetryWriter {

    private final KafkaConfiguration kafkaConfiguration;
    private final Producer<String, ErrorData> errorProducer;
    private final Producer<String, JsonNode> retryProducer;

    public ESErrorRetryWriter(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
        this.errorProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, ErrorDataSerializer.class);
        this.retryProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, JsonNodeSerialializer.class);
    }

    public void sendToErrorTopic(String applicationId, ValidateData validateData) {
        sendToErrorTopic(applicationId, toErrorData(validateData));
    }

    private ErrorData toErrorData(ValidateData validateData) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        return ErrorData.builder()
                .errorReason(validateData.getStatusCode().name())
                .errorMessage(validateData.getMessage())
                .typeValidation(validateData.getTypeValidation().name())
                .message(validateData.getMessage())
                .timestamp(df.format(new Date()))
                .build();
    }


    public void sendToErrorTopic(String applicationId, ErrorData errorData) {
        JsonNode value = JSONUtils.getInstance().parse(errorData.message);
        String type = value.has("type") ?  value.get("type").asText() : "unknown";
        Metrics.counter("skaetl_nb_produce_message_kafka_count",
                Lists.newArrayList(
                        Tag.of("processConsumerName", applicationId),
                        Tag.of("topic", kafkaConfiguration.getErrorTopic()),
                        Tag.of("type", type)
                )
        ).increment();
        Metrics.counter("skaetl_nb_produce_error_kafka_count",
                Lists.newArrayList(
                        Tag.of("processConsumerName", applicationId),
                        Tag.of("type", type),
                        Tag.of("reason", errorData.getErrorReason())
                )
        ).increment();
        errorProducer.send(new ProducerRecord<>(kafkaConfiguration.getErrorTopic(), errorData));
    }

    public void sendToRetryTopic(String applicationId, String jsonRaw) {
        sendToRetryTopic(applicationId, JSONUtils.getInstance().parse(jsonRaw));
    }

    public void sendToRetryTopic(String applicationId, JsonNode jsonNode) {
        Metrics.counter("skaetl_nb_produce_message_kafka_count",
                Lists.newArrayList(
                        Tag.of("processConsumerName", applicationId),
                        Tag.of("topic", kafkaConfiguration.getRetryTopic()),
                        Tag.of("type", jsonNode.path("type").asText())
                )
        ).increment();
        retryProducer.send(new ProducerRecord<>(kafkaConfiguration.getRetryTopic(), jsonNode));
    }
}
