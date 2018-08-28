package io.skalogs.skaetl.service;

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
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.ProcessTransformation;
import io.skalogs.skaetl.domain.TransformatorDescription;
import io.skalogs.skaetl.domain.TypeValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GenericTransformator {

    private final Map<TypeValidation, TransformatorProcess> transformators = new HashMap<>();

    public void register(TransformatorProcess transformatorProcess) {
        register(transformatorProcess.getType(),transformatorProcess);
    }

    public void register(TypeValidation typeValidation, TransformatorProcess transformatorProcess) {
        transformators.put(typeValidation,transformatorProcess);
    }

    public ObjectNode apply(JsonNode value, ProcessConsumer processConsumer) {
        ObjectNode jsonValue = (ObjectNode) value;

        if (jsonValue != null && processConsumer.getProcessTransformation() != null && !processConsumer.getProcessTransformation().isEmpty()) {
            for (ProcessTransformation pt : processConsumer.getProcessTransformation()) {
                if (transformators.containsKey(pt.getTypeTransformation())) {
                    transformators.get(pt.getTypeTransformation()).apply(processConsumer.getIdProcess(), pt.getParameterTransformation(), jsonValue);
                }
            }

        }
        return jsonValue;
    }

    public List<TransformatorDescription> transformatorFunctions() {
        return transformators
                .values()
                .stream()
                .map((e) -> new TransformatorDescription(e.getType().name(),e.getDescription()))
                .collect(Collectors.toList());
    }

}
