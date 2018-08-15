package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DateExtractorTransformator extends TransformatorProcess {

    private final Map<String,FastDateFormat> srcFormats= new HashMap<>();
    private final Map<String,DateTimeFormatter> destFormats= new HashMap<>();
    private final Cache<String, Date> srcDateCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build();
    public DateExtractorTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        String valueToFormat = at(parameterTransformation.getFormatDateValue().getKeyField(), jsonValue).asText();
        if (StringUtils.isNotBlank(valueToFormat)) {
            FastDateFormat srcFormatter = srcFormats.computeIfAbsent(parameterTransformation.getFormatDateValue().getSrcFormat(), key -> FastDateFormat.getInstance(key));
            try {
                Date asDate = srcDateCache.get(parameterTransformation.getFormatDateValue().getSrcFormat() +  "-to-" + valueToFormat, () -> srcFormatter.parse(valueToFormat));
                DateTimeFormatter dateTimeFormatter = destFormats.computeIfAbsent(parameterTransformation.getFormatDateValue().getTargetFormat(), key -> DateTimeFormatter.ofPattern(key));
                String result = asDate.toInstant().atZone(ZoneId.systemDefault()).format(dateTimeFormatter);
                put(jsonValue, parameterTransformation.getFormatDateValue().getTargetField(), result);
            } catch (ExecutionException e) {
                log.error("ExecutionException on field {} for value {}", parameterTransformation.getFormatDateValue(), jsonValue.toString());
            }

        }
    }
}
