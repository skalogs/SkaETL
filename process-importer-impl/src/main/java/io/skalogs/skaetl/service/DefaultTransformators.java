package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.transform.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Lazy(value = false)
@AllArgsConstructor
public class DefaultTransformators {
    private final GenericTransformator genericTransformator;
    private final ExternalHTTPService externalHTTPService;


    @PostConstruct
    public void initDefaults() {
        genericTransformator.register(TypeValidation.ADD_FIELD, new AddFieldTransformator());
        genericTransformator.register(TypeValidation.FORMAT_BOOLEAN, new BooleanTransformator());
        genericTransformator.register(TypeValidation.DELETE_FIELD, new DeleteFieldTransformator());
        genericTransformator.register(TypeValidation.FORMAT_DOUBLE, new DoubleFieldTransformator());
        genericTransformator.register(TypeValidation.FORMAT_DATE, new FormatDateTransformator());
        genericTransformator.register(TypeValidation.FORMAT_GEOPOINT, new GeoPointTransformator());
        genericTransformator.register(TypeValidation.FORMAT_IP, new IpFieldTransformator());
        genericTransformator.register(TypeValidation.FORMAT_LONG, new LongFieldTransformator());
        genericTransformator.register(TypeValidation.FORMAT_KEYWORD, new KeywordFieldTransformator());
        genericTransformator.register(TypeValidation.FORMAT_TEXT, new TextFieldTransformator());
        genericTransformator.register(TypeValidation.RENAME_FIELD, new RenameFieldTransformator());
        genericTransformator.register(TypeValidation.LOOKUP_LIST, new LookupListTransformator());
        genericTransformator.register(TypeValidation.HASH, new HashFieldTransformator());
        genericTransformator.register(TypeValidation.CAPITALIZE, new CapitalizeTransformator());
        genericTransformator.register(TypeValidation.UNCAPITALIZE, new UncapitalizeTransformator());
        genericTransformator.register(TypeValidation.LOWER_CASE, new LowerCaseTransformator());
        genericTransformator.register(TypeValidation.UPPER_CASE, new UpperCaseTransformator());
        genericTransformator.register(TypeValidation.SWAP_CASE, new SwapCaseTransformator());
        genericTransformator.register(TypeValidation.LOOKUP_EXTERNAL, new LookupHTTPServiceTransformator(externalHTTPService));
        genericTransformator.register(TypeValidation.ADD_GEO_LOCALISATION, new AddGeoLocalisationTransformator());
        genericTransformator.register(TypeValidation.FORMAT_EMAIL, new EmailFormatTransformator());
        genericTransformator.register(TypeValidation.ADD_CSV_LOOKUP, new AddCsvLookupTransformator());
        genericTransformator.register(TypeValidation.DATE_EXTRACTOR, new DateExtractorTransformator());
        genericTransformator.register(TypeValidation.TRANSLATE_ARRAY, new TranslateArrayTransformator());
    }
}
