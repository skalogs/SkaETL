package io.skalogs.skaetl.service;

/*-
 * #%L
 * process-importer-simulator
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


import io.skalogs.skaetl.domain.SimulateData;
import io.skalogs.skaetl.domain.ValidateData;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

public final class UtilsSimulate {

    public static SimulateData generateFromValidateData(String textInput, ValidateData validateData) {
        StringBuilder sb = new StringBuilder();
        if (validateData.errorList != null && !validateData.errorList.isEmpty()) {
            sb.append(" cause : " + validateData.errorList.stream().map(e -> e.name()).collect(Collectors.joining(" and ")));
        } else if (validateData.statusCode != null) {
            sb.append(" cause : " + validateData.statusCode);
        }
        if (validateData.message != null && StringUtils.isNotBlank(validateData.message)) {
            sb.append(validateData.message);
        }
        if (!"OK".equals(validateData.message)) {
            sb.append(" with value : " + validateData.value);
        }
        return SimulateData.builder()
                .jsonValue(validateData.jsonValue)
                .message(sb.toString())
                .value(textInput)
                .build();
    }

}
