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
        genericTransformator.register(TypeValidation.ADD_FIELD, new AddFieldTransformator(TypeValidation.ADD_FIELD));
        genericTransformator.register(TypeValidation.FORMAT_BOOLEAN, new BooleanTransformator(TypeValidation.FORMAT_BOOLEAN));
        genericTransformator.register(TypeValidation.DELETE_FIELD, new DeleteFieldTransformator(TypeValidation.DELETE_FIELD));
        genericTransformator.register(TypeValidation.FORMAT_DOUBLE, new DoubleFieldTransformator(TypeValidation.FORMAT_DOUBLE));
        genericTransformator.register(TypeValidation.FORMAT_DATE, new FormatDateTransformator(TypeValidation.FORMAT_DATE));
        genericTransformator.register(TypeValidation.FORMAT_GEOPOINT, new GeoPointTransformator(TypeValidation.FORMAT_GEOPOINT));
        genericTransformator.register(TypeValidation.FORMAT_IP, new IpFieldTransformator(TypeValidation.FORMAT_IP));
        genericTransformator.register(TypeValidation.FORMAT_LONG, new LongFieldTransformator(TypeValidation.FORMAT_LONG));
        genericTransformator.register(TypeValidation.FORMAT_KEYWORD, new KeywordFieldTransformator(TypeValidation.FORMAT_KEYWORD));
        genericTransformator.register(TypeValidation.FORMAT_TEXT, new TextFieldTransformator(TypeValidation.FORMAT_TEXT));
        genericTransformator.register(TypeValidation.RENAME_FIELD, new RenameFieldTransformator(TypeValidation.RENAME_FIELD));
        genericTransformator.register(TypeValidation.LOOKUP_LIST, new LookupListTransformator(TypeValidation.LOOKUP_LIST));
        genericTransformator.register(TypeValidation.HASH, new HashFieldTransformator(TypeValidation.HASH));
        genericTransformator.register(TypeValidation.CAPITALIZE, new CapitalizeTransformator(TypeValidation.CAPITALIZE));
        genericTransformator.register(TypeValidation.UNCAPITALIZE, new UncapitalizeTransformator(TypeValidation.UNCAPITALIZE));
        genericTransformator.register(TypeValidation.LOWER_CASE, new LowerCaseTransformator(TypeValidation.LOWER_CASE));
        genericTransformator.register(TypeValidation.UPPER_CASE, new UpperCaseTransformator(TypeValidation.UPPER_CASE));
        genericTransformator.register(TypeValidation.SWAP_CASE, new SwapCaseTransformator(TypeValidation.SWAP_CASE));
        genericTransformator.register(TypeValidation.LOOKUP_EXTERNAL, new LookupHTTPServiceTransformator(TypeValidation.LOOKUP_EXTERNAL, externalHTTPService));
        genericTransformator.register(TypeValidation.ADD_GEO_LOCALISATION, new AddGeoLocalisationTransformator(TypeValidation.ADD_GEO_LOCALISATION));
        genericTransformator.register(TypeValidation.FORMAT_EMAIL, new EmailFormatTransformator(TypeValidation.FORMAT_EMAIL));
        genericTransformator.register(TypeValidation.ADD_CSV_LOOKUP, new AddCsvLookupTransformator(TypeValidation.ADD_CSV_LOOKUP));
        genericTransformator.register(TypeValidation.DATE_EXTRACTOR, new DateExtractorTransformator(TypeValidation.DATE_EXTRACTOR));
        genericTransformator.register(TypeValidation.TRANSLATE_ARRAY, new TranslateArrayTransformator(TypeValidation.TRANSLATE_ARRAY));
    }
}
