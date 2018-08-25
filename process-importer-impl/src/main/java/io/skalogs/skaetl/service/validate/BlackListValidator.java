package io.skalogs.skaetl.service.validate;

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
            .name("skaetl_nb_message_blacklist")
            .help("nb message blacklist")
            .labelNames("fieldname")
            .register();

    public ValidateData process(ProcessValidation processValidation, JsonNode jsonValue) {

        if (processValidation.getParameterValidation().getBlackList() != null) {
            List<ProcessKeyValue> listBlack = processValidation.getParameterValidation().getBlackList()
                    .stream()
                    .filter(e -> isBlackList(jsonValue, e))
                    .collect(toList());
            if (!listBlack.isEmpty()) {
                listBlack.forEach(item -> nbMessageBlackList.labels(item.getKey() + "-" + item.getValue()).inc());
                return UtilsValidateData.createValidateData(false, StatusCode.blacklist, TypeValidation.BLACK_LIST_FIELD, jsonValue, listBlack.stream().map(e -> e.getKey() + "-" + e.getValue()).collect(Collectors.joining(";")));
            } else {
                return ValidateData.builder()
                        .success(true)
                        .typeValidation(TypeValidation.BLACK_LIST_FIELD)
                        .jsonValue(jsonValue)
                        .build();
            }
        } else {
            nbMessageBlackList.labels("empty").inc();
            return UtilsValidateData.createValidateData(false, StatusCode.blacklist, TypeValidation.BLACK_LIST_FIELD, jsonValue, "Blacklist array is null");
        }
    }


    private Boolean isBlackList(JsonNode jsonValue, ProcessKeyValue processKeyValue) {
        return at(processKeyValue.getKey(),jsonValue).asText().equals(processKeyValue.getValue());
    }
}
