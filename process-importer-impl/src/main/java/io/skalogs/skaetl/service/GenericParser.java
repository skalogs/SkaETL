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
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.service.parser.*;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GenericParser {

    private final GrokParser grokParser;
    private final CEFParser cefParser;
    private final NitroParser nitroParser;
    private final CSVParser csvParser;
    private final JsonStringParser jsonStringParser;
    private final Producer<String, String> failParserProducer;

    public GenericParser(GrokParser grokParser, CEFParser cefParser, NitroParser nitroParser, CSVParser csvParser, JsonStringParser jsonStringParser, KafkaConfiguration kafkaConfiguration) {
        this.grokParser = grokParser;
        this.cefParser = cefParser;
        this.nitroParser = nitroParser;
        this.csvParser = csvParser;
        this.jsonStringParser = jsonStringParser;
        this.failParserProducer = KafkaUtils.kafkaProducer(kafkaConfiguration.getBootstrapServers(), StringSerializer.class, StringSerializer.class);
    }

    public String apply(String value, ProcessConsumer processConsumer) {
        if (processConsumer.getProcessParser() != null && !processConsumer.getProcessParser().isEmpty()) {
            for(ProcessParser processParser : processConsumer.getProcessParser()){
                ParserResult parserResult = treatData(processParser, value);
                if(!parserResult.getFailParse()){
                    //success
                    value = parserResult.getResult();
                    break;
                }
            }
        }
        return value;
    }

    private ParserResult treatData(ProcessParser processParser, String value){
        if (processParser != null && processParser.getTypeParser()!=null) {
            switch (processParser.getTypeParser()) {
                case CEF:
                    return treatParseResult(cefParser.process(value, processParser),value, processParser);
                case NITRO:
                    return treatParseResult(nitroParser.process(value, processParser),value, processParser);
                case GROK:
                    return treatParseResult(grokParser.process(value, processParser),value, processParser);
                case CSV:
                    return treatParseResult(csvParser.process(value, processParser),value, processParser);
                case JSON_AS_STRING:
                    return treatParseResult(jsonStringParser.process(value,processParser),value, processParser);
                default:
                    log.error("Unsupported Type {}", processParser.getTypeParser());
                    return ParserResult.builder().result(value).build();
            }
        }
        return ParserResult.builder().result(value).build();
    }

    private ParserResult treatParseResult(ParserResult parserResult, String value, ProcessParser processParser){
        if(parserResult.getFailParse()){
            if(processParser.getActiveFailForward() && StringUtils.isNotBlank(processParser.getFailForwardTopic())){
                //Send into topic failForwardTopic
                failParserProducer.send(new ProducerRecord<>(processParser.getFailForwardTopic(), value));
            }
        }
        return parserResult;
    }
}
