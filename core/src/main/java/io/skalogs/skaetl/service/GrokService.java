package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.GrokDomain;
import io.skalogs.skaetl.domain.GrokResult;
import io.skalogs.skaetl.domain.GrokResultSimulate;
import io.skalogs.skaetl.repository.GrokRepository;
import io.thekraken.grok.api.Grok;
import io.thekraken.grok.api.Match;
import io.thekraken.grok.api.exception.GrokException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@Slf4j
@Component
public class GrokService {

    private Grok grokInstance;
    private final GrokRepository grokRepository;

    public GrokService(GrokRepository grokRepository){
        this.grokRepository = grokRepository;
    }

    @PostConstruct
    public void setup() {
        grokInstance = new Grok();
        loadAll();
    }

    public List<GrokDomain> findGrokPatten(String filter) {
        return grokInstance.getPatterns().entrySet().stream()
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
                grokInstance.addPattern(key,value);
                log.info("add user grok key {} value {}",key,value);
                grokRepository.save(key,value);
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
                int firstBlank = grokDataRaw.indexOf(" ");
                if(firstBlank!= -1) {
                    String key = grokDataRaw.substring(0,firstBlank);
                    String value = grokDataRaw.substring(firstBlank+1,grokDataRaw.length());
                    grokInstance.addPattern(key,value);
                }else{
                  log.error("Error in grokDataRaw {}",grokDataRaw);
                }
            } catch (GrokException e) {
                log.error("GrokException for grokDataRaw {}",grokDataRaw);
            }
        });
    }

    public List<GrokResult> simulateAllPattern(String input) {
        return grokInstance.getPatterns().entrySet().stream()
                .map(e -> parseGrok(input, "%{" + e.getKey() + "}", true))
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
                GrokResult resultItem = parseGrok(item, grokPattern, true);
                result.add(GrokResultSimulate.builder()
                        .jsonValue(resultItem.value)
                        .value(item)
                        .message("{}".equals(resultItem.value) ? "No Match" : (StringUtils.isBlank(resultItem.messageError) ? "OK" : resultItem.messageError))
                        .build());
            }
        }
        return result;
    }

    public GrokResult parseGrok(String value, String grokPattern, Boolean jsonReturn) {
        try {
            log.info("parseGrok pattern {} for value {}", grokPattern, value);
            grokInstance.compile(grokPattern);
            Match match = grokInstance.match(value);
            match.captures();
            return GrokResult.builder().value(jsonReturn ? match.toJson() : match.toString()).pattern(grokPattern).build();
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
