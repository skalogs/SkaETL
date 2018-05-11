package io.skalogs.skaetl.service.processor;

import io.skalogs.skaetl.domain.TypeOutput;
import org.apache.kafka.streams.processor.AbstractProcessor;

public abstract class AbstractOutputProcessor<K, V> extends AbstractProcessor<K, V> {

    public abstract boolean support(TypeOutput typeOutput);

}
