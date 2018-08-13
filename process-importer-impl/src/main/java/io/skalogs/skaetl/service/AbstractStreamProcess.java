package io.skalogs.skaetl.service;

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
