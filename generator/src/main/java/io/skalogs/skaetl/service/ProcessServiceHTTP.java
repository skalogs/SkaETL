package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.GeneratorConfiguration;
import io.skalogs.skaetl.domain.ProcessConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ProcessServiceHTTP {

    private final GeneratorConfiguration generatorConfiguration;

    public ProcessServiceHTTP(GeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public ProcessConsumer findProcess(String idProcess) {
        log.info("Call findProcess");
        RestTemplate restTemplate = new RestTemplate();
        ProcessConsumer obj = new ProcessConsumer();
        try {
            obj = restTemplate.getForObject(generatorConfiguration.getBackend() + "/process/findProcess?idProcess=" + idProcess, ProcessConsumer.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
        log.info("Result Call findProcess {} ", obj);
        return obj;
    }

    public void saveOrUpdate(ProcessConsumer processConsumer) {
        log.info("Call saveOrUpdate {}", processConsumer);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProcessConsumer> request = new HttpEntity<>(processConsumer);
        try {
            restTemplate.postForObject(generatorConfiguration.getBackend() + "/process/save", request, String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }

    }

    public void activateProcess(ProcessConsumer processConsumer) {
        log.info("Call activateProcess {}", processConsumer);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(generatorConfiguration.getBackend() + "/process/activate?idProcess=" + processConsumer.getIdProcess(), String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

}
