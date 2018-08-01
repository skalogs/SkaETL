package io.skalogs.skaetl.rules.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.skalogs.skaetl.domain.JoinType;
import io.skalogs.skaetl.domain.ParameterOutput;
import io.skalogs.skaetl.domain.ProcessMetric;
import io.skalogs.skaetl.domain.ProcessOutput;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.rules.metrics.processor.MetricsElasticsearchProcessor;
import io.skalogs.skaetl.rules.metrics.processor.MetricsEmailProcessor;
import io.skalogs.skaetl.rules.metrics.processor.MetricsSlackProcessor;
import io.skalogs.skaetl.rules.metrics.processor.MetricsSnmpProcessor;
import io.skalogs.skaetl.rules.metrics.serdes.MetricsSerdes;
import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;
import io.skalogs.skaetl.serdes.GenericSerdes;
import io.skalogs.skaetl.service.processor.LoggingProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.state.SessionStore;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.context.ApplicationContext;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public abstract class GenericMetricProcessor {

    private final ProcessMetric processMetric;
    private final String srcTopic;
    private final String srcTopic2;
    @Setter
    private ApplicationContext applicationContext;


    protected GenericMetricProcessor(ProcessMetric processMetric, String srcTopic) {
        this(processMetric, srcTopic, null);
    }

    protected GenericMetricProcessor(ProcessMetric processMetric, String srcTopic, String srcTopic2) {
        this.processMetric = processMetric;
        this.srcTopic = srcTopic;
        this.srcTopic2 = srcTopic2;
    }

    public KafkaStreams buildStream(Properties props) {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<Keys, JsonNode> mainStream = builder.stream(srcTopic, Consumed.with(Serdes.String(), GenericSerdes.jsonNodeSerde()))
                .filter(this::filterKey)
                .filter(this::filter)
                .selectKey(this::selectKey);


        KGroupedStream<Keys, JsonNode> filteredElementsGroupByKeys = mainStream
                .peek(this::countInput)
                .mapValues(this::mapValues)
                .groupByKey(Serialized.with(MetricsSerdes.keysSerde(), GenericSerdes.jsonNodeSerde()));

        KTable<Windowed<Keys>, Double> aggregateResults = aggregate(filteredElementsGroupByKeys);

        KStream<Keys, MetricResult> result = joinResult(builder, aggregateResults);

        result.peek(this::countOutput);
        routeResult(result);

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "metric-" + processMetric.getIdProcess() + "-stream");
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, MessageTimestampExtractor.class);

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        return streams;
    }

    private KStream<Keys, MetricResult> joinResult(StreamsBuilder builder, KTable<Windowed<Keys>, Double> aggregateResults) {
        KStream<Keys, MetricResult> result = aggregateResults
                .toStream()
                .filter(this::having)
                .map((key, value) -> new KeyValue<>(key.key(), new MetricResult(key, value)));

        if (joinType() == JoinType.NONE) {
            return result;
        }


        KStream<Keys, JsonNode> secondStream = builder.stream(srcTopic2, Consumed.with(Serdes.String(), GenericSerdes.jsonNodeSerde()))
                .filter(this::filterKeyJoin)
                .filter(this::filterJoin)
                .selectKey(this::selectKeyJoin);

        switch (joinType()) {
            case INNER:
                return result
                        .join(secondStream,
                                (metricResult, jsonNodeFromTopic2) -> join(metricResult, jsonNodeFromTopic2),
                                joinWindow(),
                                Joined.with(MetricsSerdes.keysSerde(), MetricsSerdes.metricResultSerdes(), GenericSerdes.jsonNodeSerde()));
            case OUTER:
                return result
                        .outerJoin(secondStream,
                                (metricResult, jsonNodeFromTopic2) -> join(metricResult, jsonNodeFromTopic2),
                                joinWindow(),
                                Joined.with(MetricsSerdes.keysSerde(), MetricsSerdes.metricResultSerdes(), GenericSerdes.jsonNodeSerde()));
            case LEFT:
                return result
                        .leftJoin(secondStream,
                                (metricResult, jsonNodeFromTopic2) -> join(metricResult, jsonNodeFromTopic2),
                                joinWindow(),
                                Joined.with(MetricsSerdes.keysSerde(), MetricsSerdes.metricResultSerdes(), GenericSerdes.jsonNodeSerde()));
                default:
                    throw new IllegalArgumentException(joinType() + " not supported");
        }
    }

    protected JoinType joinType() {
        return JoinType.NONE;
    }

    protected JoinWindows joinWindow() {
        return JoinWindows.of(TimeUnit.MINUTES.toMillis(1));
    }

    protected Keys selectKeyJoin(String key, JsonNode value) {
        return new Keys(processMetric.getName(), processMetric.toDSL(), value.path("project").asText());
    }

    protected boolean filterJoin(String key, JsonNode jsonNode) {
        return true;
    }

    protected boolean filterKeyJoin(String key, JsonNode jsonNode) {
        return true;
    }

    private MetricResult join(MetricResult metricResult, JsonNode fromTopic2) {
        return metricResult.withElement(fromTopic2);
    }

    private void countInput(Keys keys, JsonNode jsonNode) {
        Metrics.counter("skaetl_nb_metric_input", Lists.newArrayList(Tag.of("metricConsumerName",processMetric.getName()))).increment();
    }

    private void countOutput(Keys keys, MetricResult metricResult) {
        Metrics.counter("skaetl_nb_metric_output", Lists.newArrayList(Tag.of("metricConsumerName",processMetric.getName()))).increment();
    }

    public void routeResult(KStream<Keys, MetricResult> result) {
        for (ProcessOutput processOutput : processMetric.getProcessOutputs()) {
            switch (processOutput.getTypeOutput()) {
                case KAFKA:
                    toKafkaTopic(result,processOutput.getParameterOutput());
                    break;
                case ELASTICSEARCH:
                    toElasticsearch(result,processOutput.getParameterOutput());
                    break;
                case SYSTEM_OUT:
                    toSystemOut(result);
                    break;
                case EMAIL:
                    toEmail(result,processOutput.getParameterOutput());
                    break;
                case SLACK:
                    toSlack(result, processOutput.getParameterOutput());
                    break;
                case SNMP:
                    toSnmp(result, processOutput.getParameterOutput());
            }
        }

    }

    private void toEmail(KStream<Keys,MetricResult> result, ParameterOutput parameterOutput) {
        AbstractProcessor abstractProcessor = applicationContext.getBean(MetricsEmailProcessor.class, parameterOutput.getEmail(), parameterOutput.getTemplate());
        result.process(() -> abstractProcessor);
    }

    private void toSlack(KStream<Keys, MetricResult> result, ParameterOutput parameterOutput) {
        AbstractProcessor abstractProcessor = applicationContext.getBean(MetricsSlackProcessor.class, parameterOutput.getWebHookURL(), parameterOutput.getTemplate());
        result.process(() -> abstractProcessor);
    }

    protected void toKafkaTopic(KStream<Keys, MetricResult> result, ParameterOutput parameterOutput) {
        result.to(parameterOutput.getTopicOut(), Produced.with(MetricsSerdes.keysSerde(), MetricsSerdes.metricResultSerdes()));
    }

    protected void toElasticsearch(KStream<Keys, MetricResult> result, ParameterOutput parameterOutput) {
        AbstractProcessor abstractProcessor = applicationContext.getBean(MetricsElasticsearchProcessor.class, parameterOutput.getElasticsearchRetentionLevel());
        result.process(() -> abstractProcessor);
    }

    protected void toSystemOut(KStream<Keys, MetricResult> result) {
        AbstractProcessor abstractProcessor = applicationContext.getBean(LoggingProcessor.class);
        result.process(() -> abstractProcessor);
    }

    private void toSnmp(KStream<Keys, MetricResult> result, ParameterOutput parameterOutput) {
        AbstractProcessor abstractProcessor = applicationContext.getBean(MetricsSnmpProcessor.class);
        result.process(() -> abstractProcessor);
    }

    protected AggregateFunction aggFunction(String aggFunctionName) {
        return UDAFRegistry.getInstance().get(aggFunctionName);
    }

    // MANDATORY METHODS
    protected abstract AggregateFunction aggInitializer();

    protected JsonNode mapValues(JsonNode value) {
        return JsonNodeFactory.instance.nullNode();
    }

    protected abstract KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream);


    // OPTIONAL METHODS
    protected Double operation(AggregateFunction myOperation) {
        return (Double) myOperation.compute();
    }

    protected Keys selectKey(String key, JsonNode value) {
        return new Keys(processMetric.getName(), processMetric.toDSL(), value.path("project").asText());
    }

    protected boolean filter(String key, JsonNode value) {
        return true;
    }

    protected boolean filterKey(String key, JsonNode value) {
        return true;
    }

    protected boolean having(Windowed<Keys> keys, Double result) {
        return true;
    }

    // INTERNALS
    private Materialized<Keys, AggregateFunction, WindowStore<Bytes, byte[]>> materializedMathOperationTimeWindow() {
        return Materialized.<Keys, AggregateFunction, WindowStore<Bytes, byte[]>>as("aggregated-stream-store")
                .withKeySerde(MetricsSerdes.keysSerde())
                .withValueSerde(MetricsSerdes.aggFunctionSerdes());
    }

    private Materialized<Keys, AggregateFunction, SessionStore<Bytes, byte[]>> materializedMathOperationSessionWindow() {
        return Materialized.<Keys, AggregateFunction, SessionStore<Bytes, byte[]>>as("aggregated-stream-store")
                .withKeySerde(MetricsSerdes.keysSerde())
                .withValueSerde(MetricsSerdes.aggFunctionSerdes());
    }

    protected boolean evaluate(String functionName, Object... args) {
        return FunctionRegistry.getInstance().evaluate(functionName, args);
    }

    protected Double evaluateOperation(String functionName, Double... args) {
        return FunctionRegistry.getInstance().evaluate(functionName, args);
    }

    protected KTable<Windowed<Keys>, Double> aggregateHoppingWindow(KGroupedStream<Keys, JsonNode> kGroupedStream,
                                                                    long size,
                                                                    TimeUnit sizeUnit,
                                                                    long advanceBy,
                                                                    TimeUnit advanceByUnit) {
        TimeWindowedKStream<Keys, JsonNode> windowedKStream = kGroupedStream
                .windowedBy(TimeWindows.of(sizeUnit.toMillis(size)).advanceBy(advanceByUnit.toMillis(advanceBy)));
        KTable<Windowed<Keys>, AggregateFunction> aggregate = windowedKStream.aggregate(
                this::aggInitializer,
                (k, v, agg) -> agg.addValue(v),
                materializedMathOperationTimeWindow());


        return aggregate.mapValues(this::operation);
    }

    protected KTable<Windowed<Keys>, Double> aggregateTumblingWindow(KGroupedStream<Keys, JsonNode> kGroupedStream,
                                                                     long size,
                                                                     TimeUnit sizeUnit) {
        TimeWindowedKStream<Keys, JsonNode> windowedKStream = kGroupedStream
                .windowedBy(TimeWindows.of(sizeUnit.toMillis(size)));
        KTable<Windowed<Keys>, AggregateFunction> aggregate = windowedKStream.aggregate(
                this::aggInitializer,
                (k, v, agg) -> agg.addValue(v),
                materializedMathOperationTimeWindow());

        return aggregate.mapValues(this::operation);
    }

    protected KTable<Windowed<Keys>, Double> aggregateSessionWindow(KGroupedStream<Keys, JsonNode> kGroupedStream,
                                                                    long gap,
                                                                    TimeUnit sizeUnit) {
        SessionWindowedKStream<Keys, JsonNode> windowedKStream = kGroupedStream
                .windowedBy(SessionWindows.with(sizeUnit.toMillis(gap)));
        KTable<Windowed<Keys>, AggregateFunction> aggregate = windowedKStream.aggregate(
                this::aggInitializer,
                (k, v, agg) -> agg.addValue(v),
                (key, aggOne, aggTwo) -> aggOne.merge(aggTwo),
                materializedMathOperationSessionWindow());

        return aggregate.mapValues(this::operation);
    }

}
