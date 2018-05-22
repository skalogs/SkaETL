package io.skalogs.skaetl.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.GrokService;
import io.skalogs.skaetl.service.ProcessService;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GeneratorService {

    private final Producer<String, String> producer;
    private final GrokService grokService;
    private final ProcessService processService;
    private final String topic;
    private final ObjectMapper mapper = new ObjectMapper();
    private Random RANDOM = new Random();

    private final String[] tabDb = new String[]{
            "Oracle 11g",
            "Mysql 5.7.21"
    };
    private final String[] tabIp = new String[]{
            "10.14.15.1",
            "10.14.15.2",
            "10.14.15.3",
            "10.14.15.4",
            "10.121.120.41",
            "10.121.120.54",
            "10.121.120.64",
            "10.121.120.84"
    };
    private final String[] tabSrcIp = new String[]{
            "15.14.15.1",
            "15.14.15.2",
            "15.14.15.3"
    };
    private final String[] tabDbIp = new String[]{
            "171.14.15.1",
            "171.14.15.2"
    };

    public Date addMinutesAndSecondsToTime(int minutesToAdd, int secondsToAdd, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.MINUTE, minutesToAdd);
        cal.add(Calendar.SECOND, secondsToAdd);
        return cal.getTime();
    }


    private void createAndActiveProcessConsumer(String topic){
        if(processService.findProcess("idProcess"+topic) == null) {
            processService.saveOrUpdate(ProcessConsumer.builder()
                    .idProcess("idProcess" + topic)
                    .processInput(ProcessInput.builder().topicInput(topic).host("kafka.kafka").port("9092").build())
                    .processOutput(Lists.newArrayList(
                            ProcessOutput.builder().typeOutput(TypeOutput.ELASTICSEARCH).parameterOutput(ParameterOutput.builder().elasticsearchRetentionLevel(RetentionLevel.week).build()).build()))
                    .build());
            try {
                processService.activateProcess(processService.findProcess("idProcess" + topic));
            } catch (Exception e) {
                log.error("Exception createAndActiveProcessConsumer idProcess" + topic);
            }
        }
    }

    public void createRandomNetwork(Integer nbElem) {
        createAndActiveProcessConsumer("processtopicnetwork");
        for (int i = 0; i < nbElem; i++) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date newDate = addMinutesAndSecondsToTime(i, RANDOM.nextInt(50), new Date());
            if (i % 2 == 0) {
                sendToKafka("processtopicnetwork", RawNetworkDataGen.builder()
                        .timestamp(df.format(newDate))
                        .type("network")
                        .project("infra")
                        .messageSend(" Communication between server for timestamp" + df.format(newDate) + " for " + i)
                        .srcIp(tabIp[RANDOM.nextInt(tabIp.length)])
                        .destIp(tabIp[RANDOM.nextInt(tabIp.length)])
                        .osServer("RHEL 7.2")
                        .build());
            }
            if (i % 2 != 0) {
                sendToKafka("processtopicnetwork", RawNetworkDataGen.builder()
                        .timestamp(df.format(newDate))
                        .type("network")
                        .project("infra")
                        .messageSend(" Communication between server for timestamp" + df.format(newDate) + " for " + i)
                        .srcIp(tabSrcIp[RANDOM.nextInt(tabSrcIp.length)])
                        .databaseIp(tabDbIp[RANDOM.nextInt(tabDbIp.length)])
                        .typeDatabase(tabDb[RANDOM.nextInt(tabDb.length)])
                        .osServer("RHEL 7.2")
                        .build());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void createRandom(Integer nbElemBySlot, Integer nbSlot) {
        createAndActiveProcessConsumer("processtopic");
        for (int i = 0; i < nbSlot; i++) {
            for (int j = 0; j < nbElemBySlot; j++) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Date newDate = addMinutesAndSecondsToTime(i, RANDOM.nextInt(50), new Date());
                log.debug(i + "--" + j + "***" + df.format(newDate));
                sendToKafka("processtopic",RawDataGen.builder()
                        .timestamp(df.format(newDate))
                        .type("gnii")
                        .project("toto")
                        .messageSend(" message number " + i + "--" + j + " for timestamp" + df.format(newDate))
                        .fieldTestToDelete("GNIIIIII")
                        .fieldTestToRename("Message to rename")
                        .build());
            }
        }
    }

    public void createApacheAsText(Integer nbElemBySlot, Integer nbSlot) {
        createAndActiveProcessConsumer("apacheastext");
        try {
            Resource resource = new ClassPathResource("/access.log");
            InputStream inputstream = resource.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputstream));

            String line;
            int i = 0;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            while ((line = in.readLine()) != null) {

                Date newDate = addMinutesAndSecondsToTime(i, RANDOM.nextInt(50), new Date());

                sendToKafka("apacheastext", RawApacheTextDataGen.builder()
                        .type("apache_text")
                        .project("genere-apache-log")
                        .timestamp(df.format(newDate))
                        .message(line)
                        .build());

                if (i++ > (nbSlot * nbElemBySlot))
                    break;

                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception ex) {
            log.error("Exception generating Apache log ", ex.getMessage());
        }
    }

    public void createApacheAsJSON(Integer nbElemBySlot, Integer nbSlot) {
        createAndActiveProcessConsumer("apacheasjson");
        try {
            Resource resource = new ClassPathResource("/access.log");
            InputStream inputstream = resource.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputstream));
            String line;
            int i = 0;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            while ((line = in.readLine()) != null) {
                final java.util.Map<String, Object> capture = grokService.capture(line,"%{COMMONAPACHELOG}");

                Date newDate = addMinutesAndSecondsToTime(i, RANDOM.nextInt(50), new Date());

                sendToKafka("apacheasjson", RawApacheDataGen.builder()
                        .type("apache_json")
                        .project("genere-apache-log")
                        .timestamp(df.format(newDate))
                        .request(capture.get("request") == null ? "" : capture.get("request").toString())
                        .auth(capture.get("auth") == null ? "" : capture.get("auth").toString())
                        .bytes(capture.get("bytes") == null ? "" : capture.get("bytes").toString())
                        .clientip(capture.get("clientip") == null ? "" : capture.get("clientip").toString())
                        .httpversion(capture.get("httpversion") == null ? "" : capture.get("httpversion").toString())
                        .response(capture.get("response") == null ? "" : capture.get("response").toString())
                        .verb(capture.get("verb") == null ? "" : capture.get("verb").toString())
                        .build());

                if (i++ >= (nbSlot * nbElemBySlot))
                    break;

                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception ex) {
            log.error("Exception generating Apache log ", ex.getMessage());
        }
    }

    private void sendToKafka(String topic, RawDataGen rdg) {
        try {
            String value = mapper.writeValueAsString(rdg);
            log.info("Sending {}", value);
            producer.send(new ProducerRecord(topic, value));
        } catch (Exception e) {
            log.error("Error sending to Kafka during generation ", e);
        }
    }

    private void sendToKafka(String topic, RawNetworkDataGen rdg) {
        try {
            String value = mapper.writeValueAsString(rdg);
            log.info("Sending {}", value);
            producer.send(new ProducerRecord(topic, value));
        } catch (Exception e) {
            log.error("Error sending to Kafka during generation ", e);
        }
    }

    private void sendToKafka(String topic, RawApacheDataGen ndg) {
        try {
            String value = mapper.writeValueAsString(ndg);
            log.info("Sending {}", value);
            producer.send(new ProducerRecord(topic, value));
        } catch (Exception e) {
            log.error("Error sending to Kafka during generation ", e);
        }
    }

    private void sendToKafka(String topic, RawApacheTextDataGen ndg) {
        try {
            String value = mapper.writeValueAsString(ndg);
            log.info("Sending {}", value);
            producer.send(new ProducerRecord(topic, value));
        } catch (Exception e) {
            log.error("Error sending to Kafka during generation ", e);
        }
    }

    public GeneratorService(KafkaConfiguration kafkaConfiguration, KafkaUtils kafkaUtils, GrokService grokService, ProcessService processService ) {
        producer = kafkaUtils.kafkaProducer();
        topic = kafkaConfiguration.getTopic();
        this.grokService = grokService;
        this.processService = processService;
    }
}
