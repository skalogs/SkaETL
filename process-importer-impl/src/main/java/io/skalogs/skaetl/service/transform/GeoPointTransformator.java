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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeoPointTransformator extends TransformatorProcess {

    public GeoPointTransformator() {
        super(TypeValidation.FORMAT_GEOPOINT, "Format a field as geo point");
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        if (has(parameterTransformation.getKeyField(),jsonValue)) {
            JsonNode jsonNode = at(parameterTransformation.getKeyField(), jsonValue);
            //GeoJSON spec :)
            if (jsonNode.isArray() && parameterTransformation.isFormatGeoJson()) {
                JsonNode latitude= jsonNode.get(0);
                JsonNode longitude =jsonNode.get(1);
                ArrayNode arrayNode = (ArrayNode) jsonNode;
                arrayNode.removeAll();
                arrayNode.add(longitude);
                arrayNode.add(latitude);
            }
            put(jsonValue, parameterTransformation.getKeyField() + "_gp", jsonNode);
            remove(jsonValue,parameterTransformation.getKeyField());
        }
    }
}
