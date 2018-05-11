package io.skalogs.skaetl.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
@Slf4j
public class GeneratorErrorService {
    private final Producer<String, String> producer;
    private final String topic;
    private final ObjectMapper mapper = new ObjectMapper();
    private Random RANDOM = new Random();

    public Date addMonthToTime(int monthToAdd, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.MONTH, monthToAdd);
        return cal.getTime();
    }

    public void createRandom(Integer nbElem) {
        generateJsonLengthInvalid();
        generateJsonInvalid();
        generateMandatoryInvalid();
        generateTimestampInvalid();
        generateBlackList();
        generateNoProject();
        for (int i = 0; i < nbElem; i++) {
            int rand = RANDOM.nextInt(6);
            if (rand == 1) {
                log.info("generate generateJsonLengthInvalid");
                generateJsonLengthInvalid();
            }
            if (rand == 2) {
                log.info("generate generateJsonInvalid");
                generateJsonInvalid();
            }
            if (rand == 3) {
                log.info("generate generateMandatoryInvalid");
                generateMandatoryInvalid();
            }
            if (rand == 4) {
                log.info("generate generateTimestampInvalid");
                generateTimestampInvalid();
            }
            if (rand == 5) {
                log.info("generate generateBlackList");
                generateBlackList();
            }
            if (rand == 6) {
                log.info("generate generateNoProject");
                generateNoProject();
            }
            if (rand == 7) {
                log.info("generate raw structure");
                generateRawStructure();
            }
            if (rand == 8) {
                log.info("ggenerate raw structure");
                generateRawStructure();
            }
        }
    }

    private void generateRawStructure() {
        sendToKafka("mon message provenant du proxy");
    }

    private void generateBlackList() {
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date newDate = new Date();
        sendToKafka(RawDataGen.builder()
                .timestamp(df.format(newDate))
                .type("auditd")
                .project("project_demo")
                .messageSend(RandomStringUtils.randomAlphanumeric(60))
                .build());
    }

    private void generateJsonLengthInvalid() {
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date newDate = new Date();
        sendToKafka(RawDataGen.builder()
                .timestamp(df.format(newDate))
                .type("generateJsonLengthInvalid")
                .project("project_demo")
                .messageSend(RandomStringUtils.randomAlphanumeric(3000))
                .build());
    }

    private void generateJsonInvalid() {
        sendToKafka(RandomStringUtils.randomAlphanumeric(3000));
    }


    private void generateMandatoryInvalid() {
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date newDate = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"typ\": \"generateMandatoryInvalid\",\"timestamp\": \"" + df.format(newDate) + "\",\"project\": \"project_demo\"");
        sb.append("}");
        sendToKafka(sb.toString());
    }

    private void generateTimestampInvalid() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"type\": \"generateTimestampInvalid\",\"@timestamp\": \"\", \"project\": \"project_demo\"");
        sb.append("}");
        sendToKafka(sb.toString());
    }

    private void generateNoProject() {
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date newDate = addMonthToTime(-4, new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"type\": \"generateInFuture\",\"@timestamp\": \"" + df.format(newDate) + "\"");
        sb.append("}");
        sendToKafka(sb.toString());
    }

    private void sendToKafka(RawDataGen rdg) {
        try {
            String value = mapper.writeValueAsString(rdg);
            log.info("Sending {}", value);
            producer.send(new ProducerRecord(topic, value));
        } catch (Exception e) {
            log.error("Error during converter", e);
        }
    }

    private void sendToKafka(String raw) {
        producer.send(new ProducerRecord(topic, raw));

    }

    public GeneratorErrorService(KafkaConfiguration kafkaConfiguration, KafkaUtils kafkaUtils) {
        producer = kafkaUtils.kafkaProducer();
        topic = kafkaConfiguration.getTopic();
    }
}
