package io.skalogs.skaetl.rules.metrics.serdes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.HdrHistogram.DoubleHistogram;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DoubleHistogramDeserializer extends JsonDeserializer<DoubleHistogram> {

    @Override
    public DoubleHistogram deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(jsonParser.getBinaryValue());
        DoubleHistogram doubleHistogram = DoubleHistogram.decodeFromByteBuffer(byteBuffer, 0);
        return doubleHistogram;
    }
}
