package io.skalogs.skaetl.service.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.skalogs.skaetl.RawDataGen;
import io.skalogs.skaetl.domain.ParameterTransformation;
import io.skalogs.skaetl.domain.ProcessKeyValue;
import io.skalogs.skaetl.domain.TypeValidation;
import io.skalogs.skaetl.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RenameFieldTransformatorTest {
    @Test
    public void should_Process_Ok() throws Exception {
        RenameFieldTransformator renameFieldValidator = new RenameFieldTransformator(TypeValidation.RENAME_FIELD);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("projectvalue").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        renameFieldValidator.apply(null, ParameterTransformation.builder()
                .composeField(ProcessKeyValue.builder()
                        .key("project")
                        .value("@project")
                        .build()
                ).build(), jsonValue);
        assertThat(jsonValue.path("@project").asText()).isEqualTo("projectvalue");
    }

    @Test
    public void should_Process_Nested_Ok() throws Exception {
        RenameFieldTransformator renameFieldValidator = new RenameFieldTransformator(TypeValidation.RENAME_FIELD);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("projectvalue").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        renameFieldValidator.apply(null, ParameterTransformation.builder()
                .composeField(ProcessKeyValue.builder()
                        .key("project")
                        .value("my.project.name")
                        .build()
                ).build(), jsonValue);
        assertThat(JSONUtils.getInstance().at(jsonValue, "my.project.name").asText()).isEqualTo("projectvalue");
    }

    @Test
    public void should_Process_Nested_SameObject_Ok() throws Exception {
        RenameFieldTransformator renameFieldValidator = new RenameFieldTransformator(TypeValidation.RENAME_FIELD);
        String value = "{\"something\":\"test\",\"comment\": {\"value\":\"value1\"}}";
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        renameFieldValidator.apply(null, ParameterTransformation.builder()
                .composeField(ProcessKeyValue.builder()
                        .key("comment.value")
                        .value("comment")
                        .build()
                ).build(), jsonValue);
        assertThat(JSONUtils.getInstance().at(jsonValue, "comment").asText()).isEqualTo("value1");
    }

    @Test
    public void should_Process_Ko() throws Exception {
        RenameFieldTransformator renameFieldValidator = new RenameFieldTransformator(TypeValidation.RENAME_FIELD);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("project").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        renameFieldValidator.apply(null, ParameterTransformation.builder()
                .composeField(ProcessKeyValue.builder()
                        .key("project2")
                        .value("@project")
                        .build()
                ).build(), jsonValue);
        assertThat(jsonValue.get("@project")).isNull();
    }

    @Test
    public void should_Process_RenameEmpty() throws Exception {
        RenameFieldTransformator renameFieldValidator = new RenameFieldTransformator(TypeValidation.RENAME_FIELD);
        RawDataGen rd = RawDataGen.builder().messageSend("gni").project("").type("type").build();
        ObjectMapper obj = new ObjectMapper();
        String value = obj.writeValueAsString(rd);
        ObjectNode jsonValue = JSONUtils.getInstance().parseObj(value);

        renameFieldValidator.apply(null, ParameterTransformation.builder()
                .composeField(ProcessKeyValue.builder()
                        .key("project")
                        .value("@project")
                        .build()
                ).build(), jsonValue);
        assertThat(jsonValue.get("@project").asText()).isEqualTo("");
    }

}
