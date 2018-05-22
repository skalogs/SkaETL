package io.skalogs.skaetl.rules.metrics.udaf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.skalogs.skaetl.rules.metrics.serdes.DoubleHistogramDeserializer;
import io.skalogs.skaetl.rules.metrics.serdes.DoubleHistogramSerializer;
import lombok.Getter;
import org.HdrHistogram.DoubleHistogram;

public class MedianFunction extends AggregateFunction<JsonNode, Double> {
    @Getter
    @JsonSerialize(using = DoubleHistogramSerializer.class)
    @JsonDeserialize(using = DoubleHistogramDeserializer.class)
    private DoubleHistogram histogram= new DoubleHistogram(3600000000000L, 3);

    @Override
    public AggregateFunction addValue(JsonNode value) {
        histogram.recordValue(value.doubleValue());
        return this;
    }

    @Override
    public Double compute() {
        return histogram.getValueAtPercentile(50);
    }

    @Override
    public AggregateFunction<JsonNode, Double> merge(AggregateFunction<JsonNode, Double> newValue) {
        return compute() > newValue.compute() ? this : newValue;
    }
}
