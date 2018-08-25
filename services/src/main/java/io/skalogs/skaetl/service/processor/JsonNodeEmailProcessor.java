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
import io.skalogs.skaetl.service.EmailService;

public class JsonNodeEmailProcessor extends AbstractEmailProcessor<String, JsonNode> {

    public JsonNodeEmailProcessor(String email, EmailService emailService) {
        super(email, emailService);
    }

    public JsonNodeEmailProcessor(String email, String template, EmailService emailService) {
        super(email, template, emailService);
    }

    @Override
    protected String buildMsg(JsonNode value) {
        return value.toString();
    }

    @Override
    protected JsonNode getMsg(JsonNode value) {
        return value;
    }
}
