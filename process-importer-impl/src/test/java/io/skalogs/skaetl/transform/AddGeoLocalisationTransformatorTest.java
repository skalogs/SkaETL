package io.skalogs.skaetl.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.service.transform.AddGeoLocalisationTransformator;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AddGeoLocalisationTransformatorTest {

    @Test
    public void should_Process_Ok() throws Exception {

        AddGeoLocalisationTransformator addGeoLocalisationTransformator = new AddGeoLocalisationTransformator(TypeValidation.ADD_GEO_LOCALISATION);
        RawDataGen rd = RawDataGen.builder().messageSend("Test add Geo-Localisation").project("82.245.25.86").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);
        addGeoLocalisationTransformator.apply(null,
                ParameterTransformation.builder().
                        keyField("project").
                        build(),
                jsonValue, value);

        assertThat(jsonValue.path("project_country_name").asText()).isEqualTo("France");
    }
}
