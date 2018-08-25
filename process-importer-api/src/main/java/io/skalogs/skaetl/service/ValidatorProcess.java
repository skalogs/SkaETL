package io.skalogs.skaetl.service;

/*-
 * #%L
 * process-importer-api
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
import io.skalogs.skaetl.domain.ProcessValidation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.ValidateData;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public abstract class ValidatorProcess {

    private final TypeValidation type;

    private final JSONUtils jsonUtils = JSONUtils.getInstance();

    public abstract ValidateData process(ProcessValidation processValidation, JsonNode jsonValue);

    public Boolean type(TypeValidation typeValidation) {
        return type.equals(typeValidation);
    }

    protected boolean has(String path, JsonNode jsonNode) {
        return jsonUtils.has(jsonNode, path);
    }

    protected JsonNode at(String path, JsonNode jsonNode) {
        return jsonUtils.at(jsonNode, path);
    }
}
