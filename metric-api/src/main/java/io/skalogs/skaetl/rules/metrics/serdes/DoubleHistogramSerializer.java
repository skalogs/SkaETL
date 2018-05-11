package io.skalogs.skaetl.rules.metrics.serdes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.HdrHistogram.DoubleHistogram;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DoubleHistogramSerializer  extends JsonSerializer<DoubleHistogram> {
    @Override
    public void serialize(DoubleHistogram doubleHistogram, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        ByteBuffer allocate = ByteBuffer.allocate(doubleHistogram.getNeededByteBufferCapacity());
        doubleHistogram.encodeIntoByteBuffer(allocate);
        jsonGenerator.writeBinary(allocate.array());
    }
}
