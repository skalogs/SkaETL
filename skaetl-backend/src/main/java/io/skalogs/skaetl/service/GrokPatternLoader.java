package io.skalogs.skaetl.service;

import io.skalogs.skaetl.utils.KafkaUtils;
import io.thekraken.grok.api.Grok;
import io.thekraken.grok.api.exception.GrokException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class GrokPatternLoader {
    private final KafkaUtils kafkaUtils;

    @PostConstruct
    public void init() throws GrokException, IOException {

        Grok grok = new Grok();
        loadFromResource(grok, "/patterns/patterns");
        loadFromResource(grok, "/patterns/firewall");
        loadFromResource(grok, "/patterns/haproxy");
        loadFromResource(grok, "/patterns/java");
        loadFromResource(grok, "/patterns/linux-syslog");
        loadFromResource(grok, "/patterns/nagios");
        loadFromResource(grok, "/patterns/postfix");
        loadFromResource(grok, "/patterns/ruby");

        Map<String, String> patterns = grok.getPatterns();
        Producer<String, String> grokProducer = kafkaUtils.kafkaProducer();
        for (Map.Entry<String, String> pattern : patterns.entrySet()) {
            ProducerRecord<String, String> record = new ProducerRecord<>("grok-referential-db", pattern.getKey(), pattern.getValue());
            grokProducer.send(record);
        }
    }

    private void loadFromResource(Grok grok, String path) throws IOException, GrokException {
        log.info("Loading grok patterns from {}", path);
        Resource resource = new ClassPathResource(path);
        InputStream dbAsStream = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(dbAsStream);

        grok.addPatternFromReader(inputStreamReader);
    }
}
