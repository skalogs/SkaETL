package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.ParserResult;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.ProcessParser;
import io.skalogs.skaetl.service.parser.CEFParser;
import io.skalogs.skaetl.service.parser.CSVParser;
import io.skalogs.skaetl.service.parser.GrokParser;
import io.skalogs.skaetl.service.parser.NitroParser;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
    private final Producer<String, String> failParserProducer;

    public GenericParser(GrokParser grokParser, CEFParser cefParser, NitroParser nitroParser, CSVParser csvParser,KafkaConfiguration kafkaConfiguration) {
        this.grokParser = grokParser;
        this.cefParser = cefParser;
        this.nitroParser = nitroParser;
        this.csvParser = csvParser;
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
