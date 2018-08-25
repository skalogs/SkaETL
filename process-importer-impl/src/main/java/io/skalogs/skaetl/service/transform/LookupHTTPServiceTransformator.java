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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.domain.WorkerHTTPService;
import io.skalogs.skaetl.service.ExternalHTTPService;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

@Slf4j
public class LookupHTTPServiceTransformator extends TransformatorProcess {

    private final ExternalHTTPService externalHTTPService;

    public LookupHTTPServiceTransformator(TypeValidation type, ExternalHTTPService externalHTTPService) {
        super(type);
        this.externalHTTPService = externalHTTPService;
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        WorkerHTTPService workerHTTPService = externalHTTPService.getMapExternalService().get(idProcess);
        if (workerHTTPService != null) {
            String key = parameterTransformation.getKeyField();
            if (StringUtils.isNotBlank(key)) {
                if (has(key,jsonValue)) {
                    String oldValue = jsonValue.path(key).asText();
                    workerHTTPService.getMapResult().entrySet().stream()
                            .filter(entry -> entry.getKey().equals(oldValue))
                            .forEach(entry -> put(jsonValue, key, entry.getValue()));
                }
            } else {
                //All Keys
                workerHTTPService.getMapResult().entrySet().stream()
                        .forEach(entry -> applyMap(jsonValue, entry.getKey(), entry.getValue()));
            }
        }
    }

    private void applyMap(ObjectNode jsonValue, String oldValue, String newValue) {
        for (Iterator<Map.Entry<String, JsonNode>> it = jsonValue.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            if (entry.getValue() != null && entry.getValue().asText().equals(oldValue)) {
                //update
                put(jsonValue, entry.getKey(), newValue);
            }
        }
    }


}
