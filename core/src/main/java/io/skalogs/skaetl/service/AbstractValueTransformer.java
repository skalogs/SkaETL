package io.skalogs.skaetl.service;

import lombok.Getter;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

public abstract class AbstractValueTransformer<V,R> implements ValueTransformer<V,R> {

    @Getter
    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context =  context;
    }

    @Override
    @SuppressWarnings("deprecation")
    public R punctuate(long timestamp) {
        return null;
    }

    @Override
    public void close() {

    }
}
