package io.skalogs.skaetl.service;

import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.config.KafkaConfiguration;
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
    private final KafkaStreams errorStream;

    public ErrorImporter(ErrorToElasticsearchProcessor elasticsearchProcessor, KafkaConfiguration kafkaConfiguration, KafkaAdminService kafkaAdminService) {
        kafkaAdminService.buildTopic(kafkaConfiguration.getErrorTopic());

        StreamsBuilder builder = new StreamsBuilder();
        final Serde<ErrorData> errorDataSerde = Serdes.serdeFrom(new GenericSerializer<>(), new GenericDeserializer<>(ErrorData.class));

        KStream<String, ErrorData> streamToES = builder.stream(kafkaConfiguration.getErrorTopic(), Consumed.with(Serdes.String(), errorDataSerde));

        streamToES.process(() -> elasticsearchProcessor);

        errorStream = new KafkaStreams(builder.build(), KafkaUtils.createKStreamProperties(INPUT_PROCESS_ERROR, kafkaConfiguration.getBootstrapServers()));
        Runtime.getRuntime().addShutdownHook(new Thread(errorStream::close));
    }

    public void activate() {
        log.info("Activating error importer");
        errorStream.start();
    }

    public void deactivate() {
        log.info("Deactivating error importer");
        errorStream.close();
    }
}
