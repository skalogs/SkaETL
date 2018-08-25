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
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HashFieldTransformator extends TransformatorProcess {

    public HashFieldTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue) {
        JsonNode valueField = at(parameterTransformation.getProcessHashData().getField(), jsonValue);
        if(valueField != null &&
                StringUtils.isNotBlank(valueField.asText())){
            switch (parameterTransformation.getProcessHashData().getTypeHash()){
                case SHA256:
                    HashFunction m_hash256 = Hashing.sha256();
                    String valueHash256 = m_hash256.hashBytes(valueField.asText().getBytes()).toString();
                    put(jsonValue, parameterTransformation.getProcessHashData().getField(), valueHash256);
                    break;
                case MURMUR3:
                    HashFunction m_hashMurmur3 = Hashing.murmur3_128();
                    String valueHashMurmur3 = m_hashMurmur3.hashBytes(valueField.asText().getBytes()).toString();
                    put(jsonValue, parameterTransformation.getProcessHashData().getField(), valueHashMurmur3);
                    break;
                default:
                    log.error("Type Hash not manage {}",parameterTransformation.getProcessHashData().getTypeHash());
                    break;

            }
        }
    }
}
