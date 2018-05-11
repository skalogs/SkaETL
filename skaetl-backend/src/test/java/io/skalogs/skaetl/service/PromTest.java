package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.PrometheusConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class PromTest {
    @Test
    public void callProm() {
        PrometheusConfiguration prometheusConfiguration = new PrometheusConfiguration();
        prometheusConfiguration.setHost("35.205.180.150:9090");
        PromService promService = new PromService(prometheusConfiguration);

        promService.fetchDataCapture("nb_read_kafka_count", "processConsumerName", "demo-toto", 5).stream()
                .forEach(item -> log.error("mouarf " + item.toString()));

        promService.fetchDataCapture("nb_read_kafka_count", "processConsumerName", "cart", 5).stream()
                .forEach(item -> log.error("demo " + item.toString()));
    }

}
