package io.skalogs.skaetl.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.domain.ProcessConsumer;
import io.skalogs.skaetl.domain.ProcessTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.transform.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GenericTransformator {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExternalHTTPService externalHTTPService;
    private final Map<TypeValidation,TransformatorProcess> transformators = new HashMap<>();

    public GenericTransformator(ExternalHTTPService externalHTTPService) {
        this.externalHTTPService = externalHTTPService;
    }

    @PostConstruct
    public void init() {
        transformators.put(TypeValidation.ADD_FIELD, new AddFieldTransformator(TypeValidation.ADD_FIELD));
        transformators.put(TypeValidation.FORMAT_BOOLEAN, new BooleanTransformator(TypeValidation.FORMAT_BOOLEAN));
        transformators.put(TypeValidation.DELETE_FIELD, new DeleteFieldTransformator(TypeValidation.DELETE_FIELD));
        transformators.put(TypeValidation.FORMAT_DOUBLE, new DoubleFieldTransformator(TypeValidation.FORMAT_DOUBLE));
        transformators.put(TypeValidation.FORMAT_DATE, new FormatDateTransformator(TypeValidation.FORMAT_DATE));
        transformators.put(TypeValidation.FORMAT_GEOPOINT, new GeoPointTransformator(TypeValidation.FORMAT_GEOPOINT));
        transformators.put(TypeValidation.FORMAT_IP, new IpFieldTransformator(TypeValidation.FORMAT_IP));
        transformators.put(TypeValidation.FORMAT_LONG, new LongFieldTransformator(TypeValidation.FORMAT_LONG));
        transformators.put(TypeValidation.RENAME_FIELD, new RenameFieldTransformator(TypeValidation.RENAME_FIELD));
        transformators.put(TypeValidation.LOOKUP_LIST, new LookupListTransformator(TypeValidation.LOOKUP_LIST));
        transformators.put(TypeValidation.HASH, new HashFieldTransformator(TypeValidation.HASH));
        transformators.put(TypeValidation.CAPITALIZE, new CapitalizeTransformator(TypeValidation.CAPITALIZE));
        transformators.put(TypeValidation.UNCAPITALIZE, new UncapitalizeTransformator(TypeValidation.UNCAPITALIZE));
        transformators.put(TypeValidation.LOWER_CASE, new LowerCaseTransformator(TypeValidation.LOWER_CASE));
        transformators.put(TypeValidation.UPPER_CASE, new UpperCaseTransformator(TypeValidation.UPPER_CASE));
        transformators.put(TypeValidation.SWAP_CASE, new SwapCaseTransformator(TypeValidation.SWAP_CASE));
        transformators.put(TypeValidation.LOOKUP_EXTERNAL, new LookupHTTPServiceTransformator(TypeValidation.LOOKUP_EXTERNAL, externalHTTPService));
        transformators.put(TypeValidation.ADD_GEO_LOCALISATION, new AddGeoLocalisationTransformator(TypeValidation.ADD_GEO_LOCALISATION));
        transformators.put(TypeValidation.FORMAT_EMAIL, new EmailFormatTransformator(TypeValidation.FORMAT_EMAIL));
        transformators.put(TypeValidation.ADD_CSV_LOOKUP, new AddCsvLookupTransformator(TypeValidation.ADD_CSV_LOOKUP));
    }

    public ObjectNode apply(JsonNode value, ProcessConsumer processConsumer) {
        ObjectNode jsonValue = (ObjectNode) value;

        if (jsonValue != null && processConsumer.getProcessTransformation() != null && !processConsumer.getProcessTransformation().isEmpty()) {
            for (ProcessTransformation pt : processConsumer.getProcessTransformation()) {
                if (transformators.containsKey(pt.getTypeTransformation())) {
                    transformators.get(pt.getTypeTransformation()).apply(processConsumer.getIdProcess(),pt.getParameterTransformation(),jsonValue);
                }
            }

        }
        return jsonValue;
    }

}
