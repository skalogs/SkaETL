package io.skalogs.skaetl.service;

import io.prometheus.client.Counter;
import io.skalogs.skaetl.domain.ProcessConsumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;

import java.util.HashMap;
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
    private final HashMap<String, KafkaStreams> mapStreams = new HashMap<>();

    public static final Counter readKafkaCount = Counter.build()
            .name("skaetl_nb_read_kafka_count")
            .help("nb read")
            .labelNames("processConsumerName")
            .register();
    public static final Counter transformationAndValidationCount = Counter.build()
            .name("skaetl_nb_transformation_validation_count")
            .help("nb transfo & validation")
            .labelNames("processConsumerName")
            .register();


    public abstract void createStreamProcess();

    public void addStreams(String key, KafkaStreams streams) {
        mapStreams.put(key, streams);
    }

    public void shutdownAllStreams() {
        mapStreams.values().stream()
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
