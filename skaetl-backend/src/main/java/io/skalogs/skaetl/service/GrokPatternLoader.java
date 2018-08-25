package io.skalogs.skaetl.service;

/*-
 * #%L
 * skaetl-backend
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

import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.exception.GrokException;
import io.skalogs.skaetl.admin.KafkaAdminService;
import io.skalogs.skaetl.domain.GrokData;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.TopicConfig;
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
    private final KafkaAdminService kafkaAdminService;

    @PostConstruct
    public void init() throws GrokException, IOException {

        GrokCompiler grok = GrokCompiler.newInstance();
        loadFromResource(grok, "/patterns/patterns");
        loadFromResource(grok, "/patterns/firewall");
        loadFromResource(grok, "/patterns/haproxy");
        loadFromResource(grok, "/patterns/java");
        loadFromResource(grok, "/patterns/linux-syslog");
        loadFromResource(grok, "/patterns/nagios");
        loadFromResource(grok, "/patterns/postfix");
        loadFromResource(grok, "/patterns/ruby");

        Map<String, String> patterns = grok.getPatternDefinitions();
        final String topic = "grok-referential-db";
        kafkaAdminService.createTopic(kafkaAdminService.buildTopicInfo(topic, TopicConfig.CLEANUP_POLICY_COMPACT));
        Producer<String, GrokData> grokProducer = kafkaUtils.kafkaGrokProducer();
        for (Map.Entry<String, String> pattern : patterns.entrySet()) {
            log.info(" GrokPatternLoader Produce with key {} value {}",pattern.getKey(),pattern.getValue());
            ProducerRecord<String, GrokData> record = new ProducerRecord<>(topic, pattern.getKey(), GrokData.builder().key(pattern.getKey()).value(pattern.getValue()).build());
            grokProducer.send(record);
        }
    }

    private void loadFromResource(GrokCompiler grok, String path) throws IOException, GrokException {
        log.info("Loading grok patterns from {}", path);
        Resource resource = new ClassPathResource(path);
        InputStream dbAsStream = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(dbAsStream);

        grok.register(inputStreamReader);
    }
}
