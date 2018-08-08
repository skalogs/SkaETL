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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GenericTransformator {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExternalHTTPService externalHTTPService;
    private List<TransformatorProcess> listTransformator = new ArrayList<>();

    public GenericTransformator(ExternalHTTPService externalHTTPService) {
        this.externalHTTPService = externalHTTPService;
    }

    @PostConstruct
    public void init() {
        listTransformator.add(new AddFieldTransformator(TypeValidation.ADD_FIELD));
        listTransformator.add(new BooleanTransformator(TypeValidation.FORMAT_BOOLEAN));
        listTransformator.add(new DeleteFieldTransformator(TypeValidation.DELETE_FIELD));
        listTransformator.add(new DoubleFieldTransformator(TypeValidation.FORMAT_DOUBLE));
        listTransformator.add(new FormatDateTransformator(TypeValidation.FORMAT_DATE));
        listTransformator.add(new GeoPointTransformator(TypeValidation.FORMAT_GEOPOINT));
        listTransformator.add(new IpFieldTransformator(TypeValidation.FORMAT_IP));
        listTransformator.add(new LongFieldTransformator(TypeValidation.FORMAT_LONG));
        listTransformator.add(new RenameFieldTransformator(TypeValidation.RENAME_FIELD));
        listTransformator.add(new LookupListTransformator(TypeValidation.LOOKUP_LIST));
        listTransformator.add(new HashFieldTransformator(TypeValidation.HASH));
        listTransformator.add(new CapitalizeTransformator(TypeValidation.CAPITALIZE));
        listTransformator.add(new UncapitalizeTransformator(TypeValidation.UNCAPITALIZE));
        listTransformator.add(new LowerCaseTransformator(TypeValidation.LOWER_CASE));
        listTransformator.add(new UpperCaseTransformator(TypeValidation.UPPER_CASE));
        listTransformator.add(new SwapCaseTransformator(TypeValidation.SWAP_CASE));
        listTransformator.add(new LookupHTTPServiceTransformator(TypeValidation.LOOKUP_EXTERNAL, externalHTTPService));
        listTransformator.add(new AddGeoLocalisationTransformator(TypeValidation.ADD_GEO_LOCALISATION));
        listTransformator.add(new EmailFormatTransformator(TypeValidation.FORMAT_EMAIL));
        listTransformator.add(new AddCsvLookupTransformator(TypeValidation.ADD_CSV_LOOKUP));
    }

    public ObjectNode apply(JsonNode value, ProcessConsumer processConsumer) {
        ObjectNode jsonValue = (ObjectNode) value;
        if (jsonValue != null && processConsumer.getProcessTransformation() != null && !processConsumer.getProcessTransformation().isEmpty()) {
            for (ProcessTransformation pt : processConsumer.getProcessTransformation()) {
                listTransformator.stream()
                        .filter(e -> e.type(pt.getTypeTransformation()))
                        .forEach(e -> e.apply(processConsumer.getIdProcess(), pt.getParameterTransformation(), jsonValue));
            }

        }
        return jsonValue;
    }

}
