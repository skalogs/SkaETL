package io.skalogs.skaetl.generator;

/*-
 * #%L
 * generator
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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

;

@Component
@Slf4j
public class GeneratorErrorService {
    private final Producer<String, String> producer;
    private final String topic;
    private final ObjectMapper mapper = new ObjectMapper();
    private Random RANDOM = new Random();

    public GeneratorErrorService(KafkaUtils kafkaUtils) {
        producer = kafkaUtils.kafkaProducer();
        topic = "processtopic";
    }

    public Date addMonthToTime(int monthToAdd, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.MONTH, monthToAdd);
        return cal.getTime();
    }

    public void createRandom(Integer nbElem) {
        generateJsonLengthInvalid();
        generateJsonInvalid();
        generateMaxFieldsJsonInvalid();
        generateMandatoryInvalid();
        generateTimestampInvalid();
        generateInFuture();
        generateInPast();
        generateBlackList();
        generateNoProject();
        for (int i = 0; i < nbElem; i++) {
            int rand = RANDOM.nextInt(8);
            if (rand == 1) {
                log.info("generate generateJsonLengthInvalid");
                generateJsonLengthInvalid();
            }
            if (rand == 2) {
                log.info("generate generateJsonInvalid");
                generateJsonInvalid();
            }
            if (rand == 3) {
                log.info("generate generateMaxFieldsJsonInvalid");
                generateMaxFieldsJsonInvalid();
            }
            if (rand == 4) {
                log.info("generate generateMandatoryInvalid");
                generateMandatoryInvalid();
            }
            if (rand == 5) {
                log.info("generate generateTimestampInvalid");
                generateTimestampInvalid();
            }
            if (rand == 6) {
                log.info("generate generateInFuture");
                generateInFuture();
            }
            if (rand == 7) {
                log.info("generate generateInPast");
                generateInPast();
            }
            if (rand == 8) {
                log.info("generate generateBlackList");
                generateBlackList();
            }
            if (rand == 9) {
                log.info("generate generateNoProject");
                generateNoProject();
            }
        }
    }

    private void generateBlackList() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = new Date();
        sendToKafka(RawDataGen.builder()
                .timestamp(df.format(newDate))
                .type("auditd")
                .project("project_demo")
                .messageSend(RandomStringUtils.randomAlphanumeric(60))
                .build());
    }

    private void generateJsonLengthInvalid() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
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

    private void generateMaxFieldsJsonInvalid() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < 200; i++) {
            sb.append("\"a" + i + "\": \"value\",");
        }
        sb.append("\"project\": \"project_demo\", \"type\": \"generateMaxFieldsJsonInvalid\",\"@timestamp\": \"" + df.format(newDate) + "\"");
        sb.append("}");
        sendToKafka(sb.toString());
    }

    private void generateMandatoryInvalid() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
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

    private void generateInFuture() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMonthToTime(4, new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"type\": \"generateInFuture\",\"@timestamp\": \"" + df.format(newDate) + "\",\"project\": \"project_demo\"");
        sb.append("}");
        sendToKafka(sb.toString());
    }

    private void generateInPast() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = addMonthToTime(-4, new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"type\": \"generateInFuture\",\"@timestamp\": \"" + df.format(newDate) + "\",\"project\": \"project_demo\"");
        sb.append("}");
        sendToKafka(sb.toString());
    }

    private void generateNoProject() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
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
}
