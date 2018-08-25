package io.skalogs.skaetl.service.processor;

/*-
 * #%L
 * services
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

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.domain.TypeOutput;
import io.skalogs.skaetl.service.EmailService;
import io.skalogs.skaetl.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class AbstractEmailProcessor<K, V> extends AbstractOutputProcessor<K, V> {

    private final String email;
    private final String template;
    private final EmailService emailService;

    public AbstractEmailProcessor(String email, EmailService emailService) {
        this.email = email;
        this.emailService = emailService;
        this.template = null;
    }

    public AbstractEmailProcessor(String email, String template, EmailService emailService) {
        this.email = email;
        this.template = template;
        this.emailService = emailService;
    }

    protected abstract String buildMsg(V value);

    protected abstract JsonNode getMsg(V value);

    @Override
    public void process(K key, V value) {
        try {

            String v;

            if (!StringUtils.isBlank(template))
                v = TemplateUtils.getInstance().process(template, getMsg(value));
            else
                v = getMsg(value).toString();

            emailService.send(email, "SkETL message", v);

        } catch (Exception ex) {
            log.error("Exception during email sending {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean support(TypeOutput typeOutput) {
        return typeOutput == TypeOutput.EMAIL;
    }
}
