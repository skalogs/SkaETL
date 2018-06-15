package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.GeneratorConfiguration;
import io.skalogs.skaetl.domain.ProcessMetric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MetricServiceHTTP {
    private final GeneratorConfiguration generatorConfiguration;

    public MetricServiceHTTP(GeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public ProcessMetric findById(String idProcess) {

        RestTemplate restTemplate = new RestTemplate();
        ProcessMetric obj = new ProcessMetric();
        try {
            obj = restTemplate.getForObject(generatorConfiguration.getBackend() + "/metric/findById?idProcess=" + idProcess, ProcessMetric.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
        return obj;
    }

    public void updateProcess(ProcessMetric processMetric) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProcessMetric> request = new HttpEntity<>(processMetric);
        try {
            restTemplate.postForObject(generatorConfiguration.getBackend() + "/metric/update", request, String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

    public void activateProcess(ProcessMetric processMetric) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(generatorConfiguration.getBackend() + "/metric/activate?idProcess=" + processMetric.getIdProcess(), String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

}
