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

import io.skalogs.skaetl.domain.ConfigurationHost;
import io.skalogs.skaetl.domain.ConfigurationLogstash;
import io.skalogs.skaetl.domain.ConfigurationOutput;
import io.skalogs.skaetl.domain.Translate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UtilsConfig {

    private final String RETURN_BRACE = " }\n";
    private final String RETURN = " \n";

    public String generateConfig(ConfigurationLogstash cl) {
        StringBuilder sb = new StringBuilder();
        //input
        sb.append(" input {" + RETURN);
        cl.getInput().stream().forEach(e -> sb.append(generateInput(e)));
        sb.append("}" + RETURN);
        //filter
        sb.append(" filter {}" + RETURN);
        //output
        sb.append(" output {" + RETURN);
        cl.getOutput().stream().forEach(output -> sb.append(generateOutput(output)));
        sb.append("}" + RETURN);
        return sb.toString();
    }

    public String filterAction(Translate translate) {
        StringBuilder sb = new StringBuilder();
        if (translate != null && translate.getAction() != null && StringUtils.isNotBlank(translate.getAction().name())) {
            switch (translate.getAction()) {
                case DATE:
                    sb.append(" date { match => [\"" + translate.getKey() + "\", " + translate.getValue() + "]" + RETURN_BRACE);
                    break;
                case ADD:
                    sb.append(" mutate { add_field => {\"" + translate.getKey() + "\", \"" + translate.getValue() + "\"}" + RETURN_BRACE);
                    break;
                case DELETE:
                    sb.append(" mutate { remove_field => [\"" + translate.getKey() + "\"]" + RETURN_BRACE);
                    break;
                case GROK:
                    sb.append(" grok { match => {\"" + translate.getKey() + "\" => \"" + translate.getKeyPattern() + "\"}" + RETURN_BRACE);
                    break;
                case RENAME:
                    sb.append(" mutate { rename => {\"" + translate.getKey() + "\", \"" + translate.getValue() + "\"}" + RETURN_BRACE);
                    break;
                default:
                    log.error("Action not managed {}", translate);
                    throw new RuntimeException("Action not managed : " + translate.toString());
            }
        }
        return sb.toString();
    }

    public String generateFilter(Translate translate) {
        StringBuilder sb = new StringBuilder();
        if (translate.getTypeCondition() != null) {
            if (translate.getTypeCondition().getCheckPresent()) {
                if (translate.getTypeCondition().getIsPresence()) {
                    sb.append(" if [" + translate.getTypeCondition().getCondition());
                } else {
                    sb.append(" if ![" + translate.getTypeCondition().getCondition());
                }
                sb.append("] {" + RETURN);
                sb.append(filterAction(translate));
                sb.append(RETURN_BRACE);
            } else {
                if (!translate.getTypeCondition().getIsPresence()) {
                    sb.append(filterAction(translate));
                } else {
                    sb.append(" if [" + translate.getTypeCondition().getCondition() + "] {" + RETURN);
                    sb.append(filterAction(translate));
                    sb.append(RETURN_BRACE);
                }
            }
        } else {
            sb.append(filterAction(translate));
        }
        return sb.toString();
    }

    public String generateOutput(ConfigurationOutput output) {
        StringBuilder sb = new StringBuilder();
        //tag
        sb.append("if ("+addTags(output.getListTag())+"){"+RETURN);

        String url = StringUtils.isNotBlank(output.getPort()) ? output.getHost() + ":" + output.getPort() : output.getHost();
        sb.append(" kafka {" + RETURN);
        sb.append(" bootstrap_servers => \"" + url + "\"" + RETURN);
        sb.append(" topic_id => \"" + output.getTopic() + "\"" + RETURN);
        if (StringUtils.isNotBlank(output.getCodec())) {
            sb.append(" codec => \"" + output.getCodec() + "\"" + RETURN);
        }
        sb.append(RETURN_BRACE);
        sb.append(RETURN_BRACE);

        return sb.toString();
    }

    private String addTags(List<String> listTag){
        StringBuilder sb = new StringBuilder();
        if(listTag.size() == 1){
            sb.append("\""+listTag.get(0)+"\""+" in [tags]");
        }else{
            for (int i=0;i<listTag.size()-1;i++){
                sb.append("\""+listTag.get(i)+"\""+" in [tags] OR ");
            }
            sb.append("\""+listTag.get(listTag.size()-1)+"\""+" in [tags]");
        }
        return sb.toString();
    }

    public String generateInput(ConfigurationHost input) {
        StringBuilder sb = new StringBuilder();
        switch (input.getTypeInput()) {
            case BEATS:
                sb.append(" beats { port => " + input.getPort() + RETURN);
                sb.append(" tags => ['"+input.getTag()+"'] "+RETURN);
                if (StringUtils.isNotBlank(input.getTypeForced())) {
                    sb.append(" type => \"" + input.getTypeForced() + "\"" + RETURN_BRACE);
                } else {
                    sb.append(RETURN_BRACE);
                }
                break;
            case TCP:
                sb.append(" tcp { port => " + input.getPort() + RETURN);
                sb.append(" tags => ['"+input.getTag()+"'] "+RETURN);
                if (StringUtils.isNotBlank(input.getCodec())) {
                    sb.append(" codec => \"" + input.getCodec() + "\"" + RETURN);
                }
                if (StringUtils.isNotBlank(input.getTypeForced())) {
                    sb.append(" type => \"" + input.getTypeForced() + "\"" + RETURN_BRACE);
                } else {
                    sb.append(RETURN_BRACE);
                }
                break;
            case UDP:
                sb.append(" udp { port => " + input.getPort() + RETURN);
                sb.append(" tags => ['"+input.getTag()+"'] "+RETURN);
                if (StringUtils.isNotBlank(input.getCodec())) {
                    sb.append(" codec => \"" + input.getCodec() + "\"" + RETURN);
                }
                if (StringUtils.isNotBlank(input.getTypeForced())) {
                    sb.append(" type => \"" + input.getTypeForced() + "\"" + RETURN_BRACE);
                } else {
                    sb.append(RETURN_BRACE);
                }
                break;
            case FILE:
                sb.append(" file { path => \"" + input.getPath() + "\"" + RETURN);
                sb.append(" tags => ['"+input.getTag()+"'] "+RETURN);
                if (StringUtils.isNotBlank(input.getTypeForced())) {
                    sb.append(" type => \"" + input.getTypeForced() + "\"" + RETURN_BRACE);
                } else {
                    sb.append(RETURN_BRACE);
                }
                break;
            case KAFKA:
                String url = StringUtils.isNotBlank(input.getPort()) ? input.getHost() + ":" + input.getPort() : input.getHost();
                sb.append(" kafka {" + RETURN);
                sb.append(" bootstrap_servers => \"" + url + "\"" + RETURN);
                sb.append(" topic_id => \"" + input.getTopic() + "\"" + RETURN);
                if (StringUtils.isNotBlank(input.getCodec())) {
                    sb.append(" codec => \"" + input.getCodec() + "\"" + RETURN);
                }
                sb.append(" tags => ['"+input.getTag()+"'] "+RETURN);
                sb.append(RETURN_BRACE);
                break;
            default:
                log.error("Type not managed {}", input);
                throw new RuntimeException("Type not managed : " + input.toString());
        }
        return sb.toString();
    }
}
