package io.skalogs.skaetl.utils;

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

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TemplateUtils {

    private static TemplateUtils INSTANCE = new TemplateUtils();
    private TemplateEngine stringTemplateEngine;

    private TemplateUtils() {
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // TODO Cacheable or Not ?
        templateResolver.setCacheable(false);
        stringTemplateEngine = new TemplateEngine();
        stringTemplateEngine.addTemplateResolver(templateResolver);
    }

    public static TemplateUtils getInstance() {
        return INSTANCE;
    }

    public String process(String template, JsonNode value) {

        final Context ctx = new Context();
        List<String> fields = getVariables(template);

        for (String field : fields) {
            if (value.get(field) != null)
                ctx.setVariable(field, value.get(field).asText());
            else
                ctx.setVariable(field, "[empty]");
        }
        return stringTemplateEngine.process(template, ctx);
    }

    public String process(String template, Map values) {

        final Context ctx = new Context();
        List<String> fields = getVariables(template);

        for (String field : fields) {
            if (values.get(field) != null)
                ctx.setVariable(field, values.get(field).toString());
            else
                ctx.setVariable(field, "[empty]");
        }
        return stringTemplateEngine.process(template, ctx);
    }

    private List<String> getVariables(String template) {

        // TODO Improve the Grok Pattern to 'forget' the delimiters within the matches (and next remove the substring calls)
        final Matcher matcher = Pattern.compile("(\\$\\{(?:.)+?})").matcher(template);
        List<String> fields = new ArrayList<>();

        while (matcher.find()) {
            String var = matcher.group(1);
            fields.add(var.substring(2, var.length() - 1));
        }
        return fields;
    }
}
