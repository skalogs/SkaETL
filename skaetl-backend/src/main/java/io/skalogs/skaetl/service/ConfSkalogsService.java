package io.skalogs.skaetl.service;

/*-
 * #%L
 * skaetl-backend
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


import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.skalogs.skaetl.domain.ClientLogstash;
import io.skalogs.skaetl.domain.ConfData;
import io.skalogs.skaetl.domain.ConfigurationLogstash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public String fetch(String env, String category, String apiKey, String hostname){
        log.error("Call env {} category {} apiKey {} hostname {}",env,category,apiKey,hostname);
        if(checkData(env,category,apiKey,hostname)){
            ConfData confData = ConfData.builder().apiKey(apiKey).category(category).env(env).build();
            Metrics.counter("skaetl_fetch_skalogs_conf",
                    Lists.newArrayList(
                            Tag.of("category",category),
                            Tag.of("env", env)
                    )
            ).increment();

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
            Metrics.counter("skaetl_fetch_skalogs_conf_error").increment();
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
