package io.skalogs.skaetl.service;

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
        genericTransformator.register(new AddFieldTransformator());
        genericTransformator.register(new BooleanTransformator());
        genericTransformator.register(new DeleteFieldTransformator());
        genericTransformator.register(new DoubleFieldTransformator());
        genericTransformator.register(new FormatDateTransformator());
        genericTransformator.register(new GeoPointTransformator());
        genericTransformator.register(new IpFieldTransformator());
        genericTransformator.register(new LongFieldTransformator());
        genericTransformator.register(new KeywordFieldTransformator());
        genericTransformator.register(new TextFieldTransformator());
        genericTransformator.register(new RenameFieldTransformator());
        genericTransformator.register(new LookupListTransformator());
        genericTransformator.register(new HashFieldTransformator());
        genericTransformator.register(new CapitalizeTransformator());
        genericTransformator.register(new UncapitalizeTransformator());
        genericTransformator.register(new LowerCaseTransformator());
        genericTransformator.register(new UpperCaseTransformator());
        genericTransformator.register(new SwapCaseTransformator());
        genericTransformator.register(new LookupHTTPServiceTransformator(externalHTTPService));
        genericTransformator.register(new AddGeoLocalisationTransformator());
        genericTransformator.register(new EmailFormatTransformator());
        genericTransformator.register(new AddCsvLookupTransformator());
        genericTransformator.register(new DateExtractorTransformator());
        genericTransformator.register(new TranslateArrayTransformator());
    }
}
