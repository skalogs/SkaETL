package io.skalogs.skaetl.rules.metrics.serdes;

import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.rules.metrics.udaf.AggregateFunction;
import io.skalogs.skaetl.serdes.GenericDeserializer;
import io.skalogs.skaetl.serdes.GenericSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class MetricsSerdes {

    public static Serde<Keys> keysSerde() {
        return Serdes.serdeFrom(new GenericSerializer<Keys>(), new GenericDeserializer(Keys.class));
    }

    public static Serde<AggregateFunction> aggFunctionSerdes() {
        return Serdes.serdeFrom(new GenericSerializer<AggregateFunction>(), new GenericDeserializer(AggregateFunction.class));
    }


    public static Serde<MetricResult> metricResultSerdes() {
        return Serdes.serdeFrom(new GenericSerializer<MetricResult>(), new GenericDeserializer(MetricResult.class));
    }
}
