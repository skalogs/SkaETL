package io.skalogs.skaetl.service;


import io.prometheus.client.Gauge;
import io.skalogs.skaetl.domain.ClientLogstash;
import io.skalogs.skaetl.domain.ConfData;
import io.skalogs.skaetl.domain.ConfigurationLogstash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class ConfSkalogsService {

    private final ConfService confService;
    private final UtilsConfig utilsConfig;
    public final String[] tabEnv = {"dev","uat","integration","prod"};
    private Map<String, ClientLogstash> mapHost = new HashMap<>();

    public ConfSkalogsService(ConfService confService,UtilsConfig utilsConfig){
        this.confService=confService;
        this.utilsConfig=utilsConfig;
    }

    public static final Gauge fetchConf = Gauge.build()
            .name("skaetl_fetch_skalogs_conf")
            .help("fetch skalogs conf")
            .labelNames("category","env")
            .register();

    public static final Gauge fetchConfError = Gauge.build()
            .name("skaetl_fetch_skalogs_conf_error")
            .help("fetch skalogs conf")
            .register();

    public String fetch(String env, String category, String apiKey, String hostname){
        log.error("Call env {} category {} apiKey {} hostname {}",env,category,apiKey,hostname);
        if(checkData(env,category,apiKey,hostname)){
            ConfData confData = ConfData.builder().apiKey(apiKey).category(category).env(env).build();
            fetchConf.labels(category,env).inc();
            updateHost(hostname,env);
            ConfigurationLogstash configFecth = confService.findAll().stream()
                    .filter(cl -> cl.getConfData().equals(confData))
                    .findFirst().orElse(ConfigurationLogstash.builder().build());
            if(configFecth.statusCustomConfiguration){
                return configFecth.getCustomConfiguration();
            }else{
                return utilsConfig.generateConfig(configFecth);
            }
        }else{
            fetchConfError.inc();
            return utilsConfig.generateConfig(ConfigurationLogstash.builder().build());
        }
    }

    private Boolean checkData(String ... params){
        for(String param : params){
            if(StringUtils.isBlank(param)){
                return false;
            }
        }
        return true;
    }

    private void updateHost(String hostname,String env){
        mapHost.put(hostname,ClientLogstash.builder().env(env).hostname(hostname).dateActivity(new Date()).build());
    }

    public List<ClientLogstash> findClientLogstash(){
        return mapHost.values().stream().collect(toList());
    }

}
