package io.skalogs.skaetl.service;

/*-
 * #%L
 * core
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
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import io.krakens.grok.api.exception.GrokException;
import io.skalogs.skaetl.domain.GrokData;
import io.skalogs.skaetl.domain.GrokDomain;
import io.skalogs.skaetl.domain.GrokResult;
import io.skalogs.skaetl.domain.GrokResultSimulate;
import io.skalogs.skaetl.repository.GrokRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@Slf4j
@Component
public class GrokService {

    private final GrokCompiler grokInstance = GrokCompiler.newInstance();
    private final GrokRepository grokRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GrokService(GrokRepository grokRepository){
        this.grokRepository = grokRepository;
    }

    @PostConstruct
    public void setup() {
        grokInstance.getPatternDefinitions().clear();
        loadAll();
    }

    public List<GrokDomain> findGrokPatten(String filter) {
        return grokInstance.getPatternDefinitions().entrySet().stream()
                .filter(e -> filterGrok(e, filter))
                .map(e -> GrokDomain.builder()
                        .keyPattern(e.getKey())
                        .valuePattern(e.getValue())
                        .build())
                .collect(toList());
    }

    private Boolean filterGrok(Map.Entry<String, String> entry, String filter) {
        if (StringUtils.isNotBlank(filter)) {
            return entry.getKey().contains(filter);
        }
        return true;
    }

    public void deleteGrok(String key){
        grokRepository.deleteByKey(key);
        setup();
    }

    public void createUserGrok(String key, String value){
        if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
            try {
                grokInstance.register(key,value);
                log.info("add user grok key {} value {}",key,value);
                grokRepository.save(GrokData.builder().key(key).value(value).build());
            } catch (GrokException e) {
                log.error("GrokException for create user grok key {} value {}",key,value);
            }
        }else{
            log.error("key or value not correct for create user grok key {} value {}",key,value);
        }
    }


    private void loadAll(){
        grokRepository.findAll().stream().forEach(grokDataRaw -> {
            try {
                grokInstance.register(grokDataRaw.getKey(),grokDataRaw.getValue());
            } catch (GrokException e) {
                log.error("GrokException for grokDataRaw {}",grokDataRaw);
            }
        });
    }

    public List<GrokResult> simulateAllPattern(String input) {
        return grokInstance.getPatternDefinitions().entrySet().stream()
                .map(e -> parseGrok(input, "%{" + e.getKey() + "}"))
                .filter(e -> StringUtils.isBlank(e.messageError))
                .filter(e -> StringUtils.isNotBlank(e.value))
                .filter(e -> !e.value.equals("{}"))
                .collect(toList());
    }

    public List<GrokResultSimulate> simulate(String grokPattern, String value) {
        List<GrokResultSimulate> result = new ArrayList<>();
        if (StringUtils.isNotBlank(value)) {
            String[] tabLine = value.split("\n");
            for (String item : tabLine) {
                GrokResult resultItem = parseGrok(item, grokPattern);
                result.add(GrokResultSimulate.builder()
                        .jsonValue(resultItem.value)
                        .value(item)
                        .message("{}".equals(resultItem.value) ? "No Match" : (StringUtils.isBlank(resultItem.messageError) ? "OK" : resultItem.messageError))
                        .build());
            }
        }
        return result;
    }

    public Map<String, Object> capture(String value, String grokPattern ){
        Map<String, Object> capture = new HashMap<>();
        try {
            log.info("parseGrok pattern {} for value {}", grokPattern, value);
            Grok grok = grokInstance.compile(grokPattern);
            Match match = grok.match(value);
            capture = match.capture();
        } catch (GrokException e) {
            log.error("GrokException pattern {} message {}", grokPattern, e);
        } catch (Exception r) {
            log.error("RuntimeException GrokService {}", r);
        } finally {
            return capture;
        }
    }

    public GrokResult parseGrok(String value, String grokPattern) {
        try {
            log.info("parseGrok pattern {} for value {}", grokPattern, value);
            Grok grok = grokInstance.compile(grokPattern);
            Match match = grok.match(value);
            Map<String, Object> capture = match.capture();
            return GrokResult.builder().value(objectMapper.writeValueAsString(capture)).pattern(grokPattern).build();
        } catch (GrokException e) {
            log.error("GrokException pattern {} message {}", grokPattern, e);
            return GrokResult.builder().messageError("GrokException pattern " + grokPattern + " message " + e.getMessage()).pattern(grokPattern).build();
        } catch (Exception r) {
            log.error("RuntimeException GrokService {}", r);
            GrokResult g = GrokResult.builder().messageError("RuntimeException GrokService  message " + r.getMessage()).pattern(grokPattern).build();
            return g;
        }
    }
}
