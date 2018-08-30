package io.skalogs.skaetl.rules.metrics.processor;

/*-
 * #%L
 * metric-api
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

import io.skalogs.skaetl.rules.metrics.domain.Keys;
import io.skalogs.skaetl.rules.metrics.domain.MetricResult;
import io.skalogs.skaetl.service.EmailService;
import io.skalogs.skaetl.utils.TemplateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.processor.AbstractProcessor;

@AllArgsConstructor
@Slf4j
public class MetricsEmailProcessor extends AbstractProcessor<Keys, MetricResult> {

    private final String destinationMail;
    private final String template;
    private EmailService emailService;

    @Override
    public void process(Keys key, MetricResult value) {

        String v;

        if (!StringUtils.isBlank(template))
            v = TemplateUtils.getInstance().process(template, value.asMap());
        else
            v = value.toString();

        try {

            emailService.send(destinationMail, "SkETL message", v);

        } catch (Exception ex) {
            log.error("Exception during email sending {}", ex.getMessage());
            ex.printStackTrace();
        }

    }
}
