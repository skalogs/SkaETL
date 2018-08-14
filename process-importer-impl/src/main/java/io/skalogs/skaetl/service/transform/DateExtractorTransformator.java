package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DateExtractorTransformator extends TransformatorProcess {

    private final Map<String,DateFormat> srcFormats= new HashMap<>();
    private final Map<String,DateTimeFormatter> destFormats= new HashMap<>();
    public DateExtractorTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        String valueToFormat = at(parameterTransformation.getFormatDateValue().getKeyField(), jsonValue).asText();
        if (StringUtils.isNotBlank(valueToFormat)) {
            DateFormat srcFormatter = srcFormats.computeIfAbsent(parameterTransformation.getFormatDateValue().getSrcFormat(), key -> new SimpleDateFormat(key));
            try {
                Date asDate = srcFormatter.parse(valueToFormat);
                DateTimeFormatter dateTimeFormatter = destFormats.computeIfAbsent(parameterTransformation.getFormatDateValue().getTargetFormat(), key -> DateTimeFormatter.ofPattern(key));
                String result = asDate.toInstant().atZone(ZoneId.systemDefault()).format(dateTimeFormatter);
                put(jsonValue, parameterTransformation.getFormatDateValue().getTargetField(), result);
            } catch (ParseException e) {
                log.error("ParseException on field {} for value {}", parameterTransformation.getFormatDateValue(), jsonValue.toString());
            }

        }
    }
}
