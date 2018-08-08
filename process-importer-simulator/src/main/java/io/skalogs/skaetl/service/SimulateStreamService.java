package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.SimulateData;
import io.skalogs.skaetl.domain.StatusCode;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.rules.filters.GenericFilter;
import io.skalogs.skaetl.serdes.SimulateDataDeserializer;
import io.skalogs.skaetl.serdes.SimulateDataSerializer;
import io.skalogs.skaetl.service.processor.LoggingProcessor;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.List;

import static io.skalogs.skaetl.domain.ProcessConstants.*;
import static io.skalogs.skaetl.service.UtilsSimulate.generateFromValidateData;
import static io.skalogs.skaetl.utils.KafkaUtils.createKStreamProperties;

@Slf4j
public class SimulateStreamService extends AbstractStreamProcess {
    private final List<GenericFilter> genericFilters;

    public SimulateStreamService(GenericValidator genericValidator, GenericTransformator transformValidator, GenericParser genericParser, GenericFilterService genericFilterService, ProcessConsumer processConsumer, List<GenericFilter> genericFilters) {
        super(genericValidator, transformValidator, genericParser, genericFilterService, processConsumer);
        this.genericFilters = genericFilters;
    }

    public void createStreamProcess() {
        log.info("create Stream Process for treat INPUT");
        createStreamSimulate(getProcessConsumer().getProcessInput().getTopicInput());
        createStreamSystemOut(SIMULATE_OUTPUT);
    }

    private void createStreamSimulate(String topic) {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> streamInput = builder.stream(topic, Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, SimulateData> streamParsed = streamInput.map((key, value) -> {
            String resultParsing = getGenericParser().apply(value, getProcessConsumer());
            ObjectNode resultTransformation = getGenericTransformator().apply(JSONUtils.getInstance().parseObj(resultParsing), getProcessConsumer());
            ValidateData item = getGenericValidator().process(resultTransformation, getProcessConsumer());
            if (item.success) {
                return callFilter(value, item);
            } else {
                return new KeyValue<>("input", generateFromValidateData(value, item));
            }
        });
        final Serde<String> stringSerdes = Serdes.String();
        final Serde<SimulateData> simulateDataSerde = Serdes.serdeFrom(new SimulateDataSerializer(), new SimulateDataDeserializer());
        streamParsed.to(SIMULATE_OUTPUT, Produced.with(stringSerdes, simulateDataSerde));

        KafkaStreams streams = new KafkaStreams(builder.build(), createKStreamProperties(SIMULATE_PROCESS, getBootstrapServer()));
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }

    public void createStreamSystemOut(String topicToConsume) {

        StreamsBuilder builder = new StreamsBuilder();
        final Serde<SimulateData> simulateDataSerde = Serdes.serdeFrom(new SimulateDataSerializer(), new SimulateDataDeserializer());

        builder.stream(topicToConsume, Consumed.with(Serdes.String(), simulateDataSerde)).process(() -> new LoggingProcessor<>());

        KafkaStreams streams = new KafkaStreams(builder.build(), createKStreamProperties(SYSOUT_PROCESS, getBootstrapServer()));
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }


    private KeyValue callFilter(String value, ValidateData item) {
        //going to filters
        Boolean resultFilter = processFilter(item);
        if (resultFilter) {
            // Ok on le garde
            item.message = "OK";
            SimulateData s = generateFromValidateData(value, item);
            return new KeyValue<>("input", s);
        } else {
            // Fail on filters
            item.statusCode = StatusCode.filter_drop_message;
            return new KeyValue<>("input", generateFromValidateData(value, item));
        }
    }

    private Boolean processFilter(ValidateData item) {
        for (GenericFilter genericFilter : genericFilters) {
            if (!genericFilter.filter(item.jsonValue).getFilter()) {
                return false;
            }
        }
        return true;
    }
}
