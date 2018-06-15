package io.skalogs.skaetl.service;

import io.skalogs.skaetl.config.GeneratorConfiguration;
import io.skalogs.skaetl.domain.ProcessReferential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ReferentialServiceHTTP {

    private final GeneratorConfiguration generatorConfiguration;

    public ReferentialServiceHTTP(GeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public ProcessReferential findReferential(String idProcess) {
        RestTemplate restTemplate = new RestTemplate();
        ProcessReferential obj = new ProcessReferential();
        try {
            obj = restTemplate.getForObject(generatorConfiguration.getBackend() + "/referential/find?idReferential=" + idProcess, ProcessReferential.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
        return obj;
    }

    public void updateReferential(ProcessReferential processReferential) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProcessReferential> request = new HttpEntity<>(processReferential);
        try {
            restTemplate.postForObject(generatorConfiguration.getBackend() + "/referential/update", request, String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

    public void activateProcess(ProcessReferential processReferential) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(generatorConfiguration.getBackend() + "/referential/activate?idReferential=" + processReferential.getIdProcess(), String.class);
        } catch (Exception e) {
            log.error("status {}", e);
        }
    }

}
