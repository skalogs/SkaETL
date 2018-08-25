package io.skalogs.skaetl.service.transform;

/*-
 * #%L
 * process-importer-impl
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DateExtractorTransformator extends TransformatorProcess {

    private final Map<String,FastDateFormat> srcFormats= new HashMap<>();
    private final Map<String,DateTimeFormatter> destFormats= new HashMap<>();
    public DateExtractorTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        String valueToFormat = at(parameterTransformation.getFormatDateValue().getKeyField(), jsonValue).asText();
        if (StringUtils.isNotBlank(valueToFormat)) {
            FastDateFormat srcFormatter = srcFormats.computeIfAbsent(parameterTransformation.getFormatDateValue().getSrcFormat(), key -> FastDateFormat.getInstance(key));
            try {
                Date asDate = srcFormatter.parse(valueToFormat);
                DateTimeFormatter dateTimeFormatter = destFormats.computeIfAbsent(parameterTransformation.getFormatDateValue().getTargetFormat(), key -> DateTimeFormatter.ofPattern(key));
                String result = asDate.toInstant().atZone(ZoneId.systemDefault()).format(dateTimeFormatter);
                put(jsonValue, parameterTransformation.getFormatDateValue().getTargetField(), result);
            } catch (ParseException e) {
                log.error("ExecutionException on field {} for value {}", parameterTransformation.getFormatDateValue(), jsonValue.toString());
            }

        }
    }
}
