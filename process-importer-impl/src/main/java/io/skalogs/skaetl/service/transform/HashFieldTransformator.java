package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.TransformatorProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class HashFieldTransformator extends TransformatorProcess {

    public HashFieldTransformator(TypeValidation type) {
        super(type);
    }

    public void apply(String idProcess, ParameterTransformation parameterTransformation, ObjectNode jsonValue, String value) {
        if(jsonValue.has(parameterTransformation.getProcessHashData().getField()) &&
                StringUtils.isNotBlank(jsonValue.path(parameterTransformation.getProcessHashData().getField()).asText())){
            switch (parameterTransformation.getProcessHashData().getTypeHash()){
                case SHA256:
                    HashFunction m_hash256 = Hashing.sha256();
                    String valueHash256 = m_hash256.hashBytes(jsonValue.path(parameterTransformation.getProcessHashData().getField()).asText().getBytes()).toString();
                    jsonValue.put(parameterTransformation.getProcessHashData().getField(),valueHash256);
                    break;
                case MURMUR3:
                    HashFunction m_hashMurmur3 = Hashing.murmur3_128();
                    String valueHashMurmur3 = m_hashMurmur3.hashBytes(jsonValue.path(parameterTransformation.getProcessHashData().getField()).asText().getBytes()).toString();
                    jsonValue.put(parameterTransformation.getProcessHashData().getField(),valueHashMurmur3);
                    break;
                default:
                    log.error("Type Hash not manage {}",parameterTransformation.getProcessHashData().getTypeHash());
                    break;

            }
        }
    }
}