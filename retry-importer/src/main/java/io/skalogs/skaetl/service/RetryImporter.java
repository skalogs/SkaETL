package io.skalogs.skaetl.service;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
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
    private final KafkaStreams retryStream;

    public RetryImporter(ValidateDataToElasticSearchProcessor elasticSearchProcessor, KafkaConfiguration kafkaConfiguration, KafkaAdminService kafkaAdminService) {
        kafkaAdminService.buildTopic(kafkaConfiguration.getRetryTopic());

        StreamsBuilder builder = new StreamsBuilder();
        final Serde<ValidateData> validateDataSerdes = Serdes.serdeFrom(new ValidateDataSerializer(), new ValidateDataDeserializer());

        KStream<String, ValidateData> streamToES = builder.stream(kafkaConfiguration.getRetryTopic(), Consumed.with(Serdes.String(), validateDataSerdes));
        streamToES.process(() -> elasticSearchProcessor);

        retryStream = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties(INPUT_PROCESS_RETRY, kafkaConfiguration.getBootstrapServers()));
        Runtime.getRuntime().addShutdownHook(new Thread(retryStream::close));
    }

    public void enable() {
        log.info("Enabling retry importer");
        retryStream.start();
    }

    public void disable() {
        log.info("Disabling retry importer");
        retryStream.close();
    }
}