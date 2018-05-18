package io.skalogs.skaetl.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.skalogs.skaetl.config.KafkaConfiguration;
import io.skalogs.skaetl.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
@Slf4j
public class GeneratorRetryService {

    private final Producer<String, String> producer;
    private final String topic;
    private final ObjectMapper mapper = new ObjectMapper();
    private Random RANDOM = new Random();

    public Date addMinutesAndSecondsToTime(int minutesToAdd, int secondsToAdd, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.MINUTE, minutesToAdd);
        cal.add(Calendar.SECOND, secondsToAdd);
        return cal.getTime();
    }

    public void createRandom(Integer nbElemBySlot, Integer nbSlot) {

        for (int i = 0; i < nbSlot; i++) {
            for (int j = 0; j < nbElemBySlot; j++) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Date newDate = addMinutesAndSecondsToTime(i, RANDOM.nextInt(50), new Date());
                log.debug(i + "--" + j + "***" + df.format(newDate));
                sendToKafka(RawDataGen.builder()
                        .timestamp(df.format(newDate))
                        .type("gnii")
                        .messageSend(" message number " + i + "--" + j + " for timestamp" + df.format(newDate))
                        .build());
            }
        }
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

    public GeneratorRetryService(KafkaConfiguration kafkaConfiguration, KafkaUtils kafkaUtils) {
        producer = kafkaUtils.kafkaProducer();
        topic = kafkaConfiguration.getRetryTopic();
    }
}