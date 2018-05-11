package io.skalogs.skaetl.service.validate;

import com.fasterxml.jackson.databind.JsonNode;
import io.prometheus.client.Counter;
import io.skalogs.skaetl.domain.*;
import io.skalogs.skaetl.service.UtilsValidateData;
import io.skalogs.skaetl.service.ValidatorProcess;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
public class BlackListValidator extends ValidatorProcess {

    public BlackListValidator(TypeValidation type) {
        super(type);
    }

    private static final Counter nbMessageBlackList = Counter.build()
            .name("nb_message_blacklist")
            .help("nb message blacklist")
            .labelNames("fieldname")
            .register();

    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue, String value) {

        if (processValidation.getParameterValidation().getBlackList() != null) {
            List<ProcessKeyValue> listBlack = processValidation.getParameterValidation().getBlackList()
                    .stream()
                    .filter(e -> isBlackList(jsonValue, e))
                    .collect(toList());
            if (!listBlack.isEmpty()) {
                listBlack.forEach(item -> nbMessageBlackList.labels(item.getKey() + "-" + item.getValue()).inc());
                return UtilsValidateData.createValidateData(false, StatusCode.blacklist, TypeValidation.BLACK_LIST_FIELD, value, listBlack.stream().map(e -> e.getKey() + "-" + e.getValue()).collect(Collectors.joining(";")));
            } else {
                return ValidateData.builder()
                        .success(true)
                        .typeValidation(TypeValidation.BLACK_LIST_FIELD)
                        .jsonValue(jsonValue)
                        .build();
            }
        } else {
            nbMessageBlackList.labels("empty").inc();
            return UtilsValidateData.createValidateData(false, StatusCode.blacklist, TypeValidation.BLACK_LIST_FIELD, value, "Blacklist array is null");
        }
    }


    private Boolean isBlackList(JsonNode jsonValue, ProcessKeyValue processKeyValue) {
        return jsonValue.path(processKeyValue.getKey()).asText().equals(processKeyValue.getValue());
    }
}
