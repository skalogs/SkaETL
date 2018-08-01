package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.ProcessFilter;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GenericFilterService {

    private final Producer<String, String> failFilterProducer;

    public GenericFilterService(KafkaConfiguration kafkaConfiguration) {
        this.failFilterProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, StringSerializer.class);
    }
    public void treatParseResult(ProcessFilter processFilter, JsonNode value){
        if(value !=null) {
            failFilterProducer.send(new ProducerRecord<>(processFilter.getFailForwardTopic(), value.toString()));
        }
    }
}
