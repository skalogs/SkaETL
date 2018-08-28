package io.skalogs.skaetl.service;

/*-
 * #%L
 * process-importer-impl
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

import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.repository.ParserDescriptionRepository;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class GenericParser {

    private final Producer<String, String> failParserProducer;
    private final ParserDescriptionRepository parserDescriptionRepository;
    private final Map<TypeParser, ParserProcess> parsers = new HashMap<>();

    public GenericParser(KafkaConfiguration kafkaConfiguration, ParserDescriptionRepository parserDescriptionRepository) {
        this.failParserProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, StringSerializer.class);
        this.parserDescriptionRepository = parserDescriptionRepository;
    }

    public void register(ParserProcess parserProcess) {
        register(parserProcess.getTypeParser(), parserProcess);
    }

    public void register(TypeParser typeParser, ParserProcess parserProcess) {
        parsers.put(typeParser, parserProcess);
        parserDescriptionRepository.save(new ParserDescription(parserProcess.getTypeParser().name(),parserProcess.getDescription()));
    }

    public String apply(String value, ProcessConsumer processConsumer) {
        if (processConsumer.getProcessParser() != null && !processConsumer.getProcessParser().isEmpty()) {
            for (ProcessParser processParser : processConsumer.getProcessParser()) {
                ParserResult parserResult = treatData(processParser, value);
                if (!parserResult.getFailParse()) {
                    //success
                    value = parserResult.getResult();
                    break;
                }
            }
        }
        return value;
    }

    private ParserResult treatData(ProcessParser processParser, String value) {
        if (processParser != null && processParser.getTypeParser() != null) {
            if (parsers.containsKey(processParser.getTypeParser())) {
                ParserProcess parserProcess = parsers.get(processParser.getTypeParser());
                return treatParseResult(parserProcess.process(value, processParser), value, processParser);
            } else {
                log.error("Unsupported Type {}", processParser.getTypeParser());
            }
        }
        return ParserResult.builder().result(value).build();
    }

    private ParserResult treatParseResult(ParserResult parserResult, String value, ProcessParser processParser) {
        if (parserResult.getFailParse()) {
            if (processParser.getActiveFailForward() && StringUtils.isNotBlank(processParser.getFailForwardTopic())) {
                //Send into topic failForwardTopic
                failParserProducer.send(new ProducerRecord<>(processParser.getFailForwardTopic(), value));
            }
        }
        return parserResult;
    }

}
