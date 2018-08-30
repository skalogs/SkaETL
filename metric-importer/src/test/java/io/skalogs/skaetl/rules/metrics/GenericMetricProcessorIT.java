package io.skalogs.skaetl.rules.metrics;

/*-
 * #%L
 * metric-importer
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.kafka.KafkaUnit;
import io.skalogs.skaetl.kafka.KafkaUnitRule;
import io.skalogs.skaetl.rules.UtilsValidator;
import io.skalogs.skaetl.rules.functions.FunctionRegistry;
import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.rules.metrics.repository.AggFunctionDescriptionRepository;
import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;
import io.skalogs.skaetl.rules.repository.FilterFunctionDescriptionRepository;
import io.skalogs.skaetl.serdes.JsonNodeSerialializer;
import io.skalogs.skaetl.utils.JSONUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Windowed;
import org.assertj.core.util.Lists;
import org.junit.*;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericMetricProcessorIT {
    private final FunctionRegistry functionRegistry = new FunctionRegistry(Mockito.mock(FilterFunctionDescriptionRepository.class));
    private final UDAFRegistry udafRegistry = new UDAFRegistry(Mockito.mock(AggFunctionDescriptionRepository.class));
    @ClassRule
    public static KafkaUnitRule kafkaUnitRule = new KafkaUnitRule();

    private Properties properties = new Properties();

    private KafkaStreams streams;

    @Before
    public void beforeEachTest() {
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUnitRule.getKafkaUnit().getKafkaConnect());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1);
        properties.put(StreamsConfig.POLL_MS_CONFIG, 10);
        properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, MessageTimestampExtractor.class);

    }

    @After
    public void closeStream() {
        if (streams != null) {
            streams.close();
        }
    }

    @Test
    public void shouldComputeCount() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 10}")
        );
        String destTopic = "count-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("count", destTopic), "count-src", functionRegistry, udafRegistry) {
            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("count");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {                return value.path("duration");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        assertThat(resultInDestTopic.get(0).getKey().getRuleName()).isEqualTo("count");
        assertThat(resultInDestTopic.get(0).getKey().getRuleDSL()).isNotBlank();
        assertThat(resultInDestTopic.get(0).getKey().getProject()).isEqualTo("myproject");
        assertThat(resultInDestTopic.get(0).getValue().getResult()).isEqualTo(2);
    }

    @Test
    public void shouldComputeCountDistinct() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 10}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 11}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 12}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 13}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"anotherone\",\"duration\": 13}")

        );
        String destTopic = "count-distinct-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("count-distinct", destTopic), "count-distinct-src", functionRegistry, udafRegistry) {
            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("count-distinct");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {                return value.path("type");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        assertThat(resultInDestTopic.get(0).getKey().getRuleName()).isEqualTo("count-distinct");
        assertThat(resultInDestTopic.get(0).getKey().getRuleDSL()).isNotBlank();
        assertThat(resultInDestTopic.get(0).getKey().getProject()).isEqualTo("myproject");
        assertThat(resultInDestTopic.get(0).getValue().getResult()).isEqualTo(3);
    }

    private ProcessMetric buildProcessMetric(String name, String destTopic) {
        ProcessOutput output = ProcessOutput.builder()
                .typeOutput(TypeOutput.KAFKA)
                .parameterOutput(
                        ParameterOutput.builder()
                                .topicOut(destTopic)
                                .build()
                )
                .build();

        return ProcessMetric.builder()
                .name(name)
                .windowType(WindowType.HOPPING)
                .size(1)
                .sizeUnit(TimeUnit.SECONDS)
                .processOutputs(Lists.newArrayList(output)).build();
    }

    @Test
    public void shouldComputeMin() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 10}")
        );
        String destTopic = "min-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("min", destTopic), "min-src", functionRegistry, udafRegistry) {
            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("min");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        assertThat(resultInDestTopic.get(0).getKey().getRuleName()).isEqualTo("min");
        assertThat(resultInDestTopic.get(0).getKey().getRuleDSL()).isNotBlank();
        assertThat(resultInDestTopic.get(0).getKey().getProject()).isEqualTo("myproject");
        assertThat(resultInDestTopic.get(0).getValue().getResult()).isEqualTo(1);
    }

    @Test
    public void shouldComputeMax() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 10}")
        );
        String destTopic = "max-dest";

        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("max", destTopic), "max-src", functionRegistry, udafRegistry) {

            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("max");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getKey().getRuleName()).isEqualTo("max");
        assertThat(result1.getKey().getRuleDSL()).isNotBlank();
        assertThat(result1.getKey().getProject()).isEqualTo("myproject");
        assertThat(result1.getValue().getResult()).isEqualTo(10);
    }

    @Test
    public void shouldComputeAvg() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 9}")
        );


        String destTopic = "avg-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("avg", destTopic), "avg-src", functionRegistry, udafRegistry) {

            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("avg");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getKey().getRuleName()).isEqualTo("avg");
        assertThat(result1.getKey().getRuleDSL()).isNotBlank();
        assertThat(result1.getKey().getProject()).isEqualTo("myproject");
        assertThat(result1.getValue().getResult()).isEqualTo(5);
    }

    @Test
    public void shouldComputeSum() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 9}")
        );


        String destTopic = "sum-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("sum", destTopic), "sum-src", functionRegistry, udafRegistry) {

            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("sum");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getKey().getRuleName()).isEqualTo("sum");
        assertThat(result1.getKey().getRuleDSL()).isNotBlank();
        assertThat(result1.getKey().getProject()).isEqualTo("myproject");
        assertThat(result1.getValue().getResult()).isEqualTo(10);
    }

    @Test
    public void shouldComputeMedian() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 2}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 3}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 4}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 5}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 6}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 100}")
        );


        String destTopic = "median-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("median", destTopic), "median-src", functionRegistry, udafRegistry) {

            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("median");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getKey().getRuleName()).isEqualTo("median");
        assertThat(result1.getKey().getRuleDSL()).isNotBlank();
        assertThat(result1.getKey().getProject()).isEqualTo("myproject");
        assertThat(result1.getValue().getResult()).isEqualTo(4.0029296875);
    }


    @Test
    public void shouldComputeSumGroupByType() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 9}")
        );


        String destTopic = "sum-groupby-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("sum-groupby", destTopic), "sum-groupby-src", functionRegistry, udafRegistry) {

            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("sum");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }

            @Override
            protected boolean filterKey(String key, JsonNode value) {
                return value.hasNonNull("type");
            }

            @Override
            protected Keys selectKey(String key, JsonNode value) {
                Keys keys = super.selectKey(key, value);
                keys.addKey("type", value.get("type").asText());
                return keys;
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(2);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getKey().getRuleName()).isEqualTo("sum-groupby");
        assertThat(result1.getKey().getRuleDSL()).isNotBlank();
        assertThat(result1.getKey().getProject()).isEqualTo("myproject");
        assertThat(result1.getKey().getKeys().get("type")).isEqualTo("something");
        assertThat(result1.getValue().getResult()).isEqualTo(1);
        //assertThat(resultInDestTopic.get("type:something")).isEqualTo(1d);
        assertThat(resultInDestTopic).hasSize(2);
        KafkaUnit.Message<Keys, MetricResult> result2 = resultInDestTopic.get(1);
        assertThat(result2.getKey().getRuleName()).isEqualTo("sum-groupby");
        assertThat(result2.getKey().getRuleDSL()).isNotBlank();
        assertThat(result2.getKey().getProject()).isEqualTo("myproject");
        assertThat(result2.getKey().getKeys().get("type")).isEqualTo("somethingelse");
        assertThat(result2.getValue().getResult()).isEqualTo(9);
    }

    @Test
    public void shouldComputeCountGroupByType() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 2}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 3}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 9}")
        );


        String destTopic = "count-groupby-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("count-groupby", destTopic), "count-groupby-src", functionRegistry, udafRegistry) {

            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("count");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }

            @Override
            protected boolean filterKey(String key, JsonNode value) {
                return value.hasNonNull("type");
            }

            @Override
            protected Keys selectKey(String key, JsonNode value) {
                Keys keys = super.selectKey(key, value);
                keys.addKey("type", value.get("type").asText());
                return keys;
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(2);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getKey().getRuleName()).isEqualTo("count-groupby");
        assertThat(result1.getKey().getRuleDSL()).isNotBlank();
        assertThat(result1.getKey().getProject()).isEqualTo("myproject");
        assertThat(result1.getKey().getKeys().get("type")).isEqualTo("something");
        assertThat(result1.getValue().getResult()).isEqualTo(3);
        //assertThat(resultInDestTopic.get("type:something")).isEqualTo(1d);
        assertThat(resultInDestTopic).hasSize(2);
        KafkaUnit.Message<Keys, MetricResult> result2 = resultInDestTopic.get(1);
        assertThat(result2.getKey().getRuleName()).isEqualTo("count-groupby");
        assertThat(result2.getKey().getRuleDSL()).isNotBlank();
        assertThat(result2.getKey().getProject()).isEqualTo("myproject");
        assertThat(result2.getKey().getKeys().get("type")).isEqualTo("somethingelse");
        assertThat(result2.getValue().getResult()).isEqualTo(1);
    }


    @Test
    @Ignore("slow")
    public void shouldComputeCountWithJoin() {

        List<JsonNode> operationCB = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"user\":\"christophe\",\"code_retour_cb\":\"OK\"}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"sonexfemme\",\"code_retour_cb\":\"KO\"}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"sonexfemme\",\"code_retour_cb\":\"KO\"}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"sonexfemme\",\"code_retour_cb\":\"KO\"}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"nico\",\"code_retour_cb\":\"KO\"}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"nico\",\"code_retour_cb\":\"KO\"}")
        );

        List<JsonNode> payment = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"user\":\"christophe\",\"montant\": 42}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"nico\",\"montant\": 224}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"sonexfemme\",\"montant\": 10000}"),
                toJsonNode("{\"project\":\"myproject\",\"user\":\"sonexfemme\",\"montant\": 5000}")
        );


        String destTopic = "count-cb-ko-join-payment";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("count-incident-payment", destTopic), "operation-cb", "payment", functionRegistry, udafRegistry) {

            @Override
            protected JoinType joinType() {
                return JoinType.INNER;
            }

            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("count");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.at("/a/duration");
            }

            @Override
            protected Keys selectKey(String key, JsonNode value) {
                Keys keys = super.selectKey(key, value);
                keys.addKey("join", value.get("user").asText());
                return keys;
            }

            @Override
            protected boolean filter(String key, JsonNode value) {
                return value.path("code_retour_cb").asText().equalsIgnoreCase("KO");
            }

            @Override
            protected Keys selectKeyJoin(String key, JsonNode value) {
                Keys keys = super.selectKey(key, value);
                keys.addKey("join", value.get("user").asText());
                return keys;
            }

            protected JoinWindows joinWindow() {
                return JoinWindows.of(TimeUnit.SECONDS.toMillis((long) 1));
            }

            @Override
            protected boolean having(Windowed<Keys> keys, Double result) {
                return result > 2;
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricJoinStream(operationCB, payment, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(2);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getValue().getResult()).isEqualTo(3);
    }


    @Test
    public void shouldComputeSumWithFilter() {

        List<JsonNode> input = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 42}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 9}")
        );


        String destTopic = "sum-withfilter-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("sum-withfilter", destTopic), "sum-withfilter-src", functionRegistry, udafRegistry) {
            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("sum");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }

            @Override
            protected boolean filter(String key, JsonNode value) {
                return evaluate("CONTAINS", UtilsValidator.get(value, "type"), "else");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(1);
        KafkaUnit.Message<Keys, MetricResult> result1 = resultInDestTopic.get(0);
        assertThat(result1.getKey().getRuleName()).isEqualTo("sum-withfilter");
        assertThat(result1.getKey().getRuleDSL()).isNotBlank();
        assertThat(result1.getKey().getProject()).isEqualTo("myproject");
        assertThat(result1.getValue().getResult()).isEqualTo(10);
    }

    @Test
    @Ignore("slow")
    public void shouldComputeMinWithTumblingWindow() {

        List<JsonNode> inputStartTime = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 2}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 10}")
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<JsonNode> inputsDelayed = Arrays.asList(
                toJsonNode("{\"project\":\"myproject\",\"type\":\"something\",\"duration\": 1}"),
                toJsonNode("{\"project\":\"myproject\",\"type\":\"somethingelse\",\"duration\": 8}")
        );
        List<JsonNode> input = new ArrayList<>();
        input.addAll(inputStartTime);
        input.addAll(inputsDelayed);

        String destTopic = "min-dest";
        GenericMetricProcessor minDuration = new GenericMetricProcessor(buildProcessMetric("min",destTopic), "dsl", "min-src", functionRegistry, udafRegistry) {
            @Override
            protected AggregateFunction aggInitializer() {
                return aggFunction("min");
            }

            @Override
            protected KTable<Windowed<Keys>, Double> aggregate(KGroupedStream<Keys, JsonNode> kGroupedStream) {
                return aggregateTumblingWindow(kGroupedStream, 1, TimeUnit.SECONDS);
            }

            @Override
            protected JsonNode mapValues(JsonNode value) {
                return value.path("duration");
            }
        };
        List<KafkaUnit.Message<Keys, MetricResult>> resultInDestTopic = executeMetricStream(input, minDuration, destTopic);
        assertThat(resultInDestTopic).hasSize(2);
        assertThat(resultInDestTopic.get(0).getKey().getRuleName()).isEqualTo("min");
        assertThat(resultInDestTopic.get(0).getKey().getRuleDSL()).isNotBlank();
        assertThat(resultInDestTopic.get(0).getKey().getProject()).isEqualTo("myproject");
        assertThat(resultInDestTopic.get(0).getValue().getResult()).isEqualTo(2);
        assertThat(resultInDestTopic.get(1).getKey().getRuleName()).isEqualTo("min");
        assertThat(resultInDestTopic.get(1).getKey().getRuleDSL()).isNotBlank();
        assertThat(resultInDestTopic.get(1).getKey().getProject()).isEqualTo("myproject");
        assertThat(resultInDestTopic.get(1).getValue().getResult()).isEqualTo(1);

    }


    private List<KafkaUnit.Message<Keys, MetricResult>> executeMetricStream(List<JsonNode> inputs, GenericMetricProcessor metricProcessor, String destTopic) {
        kafkaUnitRule.getKafkaUnit().createTopic(metricProcessor.getSrcTopic());
        kafkaUnitRule.getKafkaUnit().createTopic(destTopic);
        kafkaUnitRule.getKafkaUnit().sendMessages(StringSerializer.class.getName(), JsonNodeSerialializer.class.getName(),
                toProducerRecords(metricProcessor.getSrcTopic(), inputs));


        executeStream(metricProcessor.buildStream(properties));

        MetricResultMessageExtractor messageExtractor = new MetricResultMessageExtractor();
        return kafkaUnitRule.getKafkaUnit().readMessages(destTopic, 10, messageExtractor);
    }

    private List<KafkaUnit.Message<Keys, MetricResult>> executeMetricJoinStream(List<JsonNode> inputs, List<JsonNode> inputs2, GenericMetricProcessor metricProcessor, String destTopic) {
        kafkaUnitRule.getKafkaUnit().createTopic(metricProcessor.getSrcTopic());
        kafkaUnitRule.getKafkaUnit().createTopic(destTopic);
        kafkaUnitRule.getKafkaUnit().createTopic(metricProcessor.getSrcTopic2());

        kafkaUnitRule.getKafkaUnit().sendMessages(StringSerializer.class.getName(), JsonNodeSerialializer.class.getName(),
                toProducerRecords(metricProcessor.getSrcTopic(), inputs));
        kafkaUnitRule.getKafkaUnit().sendMessages(StringSerializer.class.getName(), JsonNodeSerialializer.class.getName(),
                toProducerRecords(metricProcessor.getSrcTopic2(), inputs2));

        executeStream(metricProcessor.buildStream(properties));

        MetricResultMessageExtractor messageExtractor = new MetricResultMessageExtractor();
        return kafkaUnitRule.getKafkaUnit().readMessages(destTopic, 10, messageExtractor);
    }

    private List<ProducerRecord<String, JsonNode>> toProducerRecords(String topic, List<JsonNode> inputs) {
        return inputs.stream().map(e -> new ProducerRecord<>(topic, "something", e)).collect(Collectors.toList());
    }

    private JsonNode toJsonNode(String rawJson) {
        ObjectNode parse = JSONUtils.getInstance().parseObj(rawJson);
        parse.put("timestamp", ISO8601Utils.format(new Date()));
        return parse;
    }

    private void executeStream(KafkaStreams streams) {
        this.streams = streams;
        streams.cleanUp();
        streams.start();
    }


}
