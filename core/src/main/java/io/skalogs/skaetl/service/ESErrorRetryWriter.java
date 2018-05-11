package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.prometheus.client.Counter;
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
    private static final Counter producerErrorKafkaCount = Counter.build()
            .name("nb_produce_error_kafka_count")
            .help("count nb error elements.")
            .labelNames("processConsumerName", "type", "reason")
            .register();
    private static final Counter produceMessageToKafka = Counter.build()
            .name("nb_produce_message_kafka_count")
            .help("count nb produce kafka")
            .labelNames("processConsumerName", "topic", "type")
            .register();

    public ESErrorRetryWriter(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
        this.errorProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, ErrorDataSerializer.class);
        this.retryProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, JsonNodeSerialializer.class);
    }

    public void sendToErrorTopic(String applicationId, ValidateData validateData) {
        sendToErrorTopic(applicationId,toErrorData(validateData));
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
        producerErrorKafkaCount.labels(applicationId, errorData.getTypeValidation(), errorData.getErrorReason()).inc();
        produceMessageToKafka.labels(applicationId, kafkaConfiguration.getErrorTopic(), errorData.getTypeValidation()).inc();
        errorProducer.send(new ProducerRecord<>(kafkaConfiguration.getErrorTopic(), errorData));
    }

    public void sendToRetryTopic(String applicationId, String jsonRaw) {
        sendToRetryTopic(applicationId, JSONUtils.getInstance().parse(jsonRaw));
    }

    public void sendToRetryTopic(String applicationId, JsonNode jsonNode) {
        produceMessageToKafka.labels(applicationId, kafkaConfiguration.getRetryTopic(), jsonNode.path("type").asText()).inc();
        retryProducer.send(new ProducerRecord<>(kafkaConfiguration.getRetryTopic(), jsonNode));
    }
}
