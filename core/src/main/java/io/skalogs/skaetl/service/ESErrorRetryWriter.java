package io.skalogs.skaetl.service;

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
        Metrics.counter("skaetl_nb_produce_message_kafka_count",
                Lists.newArrayList(
                        Tag.of("processConsumerName", applicationId),
                        Tag.of("topic", kafkaConfiguration.getErrorTopic()),
                        Tag.of("type", errorData.getTypeValidation())
                )
        ).increment();
        Metrics.counter("skaetl_nb_produce_error_kafka_count",
                Lists.newArrayList(
                        Tag.of("processConsumerName", applicationId),
                        Tag.of("type", errorData.getTypeValidation()),
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
