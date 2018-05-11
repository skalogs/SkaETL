package io.skalogs.skaetl.rules.functions;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.rules.UtilsValidator;
import org.junit.Test;

import static io.skalogs.skaetl.rules.JSONUtils.createJsonNode;
import static org.assertj.core.api.Assertions.assertThat;

public class FunctionRegistryTest {
    @Test
    public void contains_OK() {
        String test = "{\"key1\": \"blablablabla toto blablabla\"}";
        JsonNode jsonNode = createJsonNode(test);
        assertThat(evaluate("CONTAINS", UtilsValidator.get(jsonNode, "key1"), "toto")).isTrue();
    }

    @Test
    public void contains_KO() {
        String test = "{\"key1\": \"blablablabla toto blablabla\"}";
        JsonNode jsonNode = createJsonNode(test);
        assertThat(evaluate("CONTAINS", UtilsValidator.get(jsonNode, "key1"), "titi")).isFalse();
        assertThat(evaluate("CONTAINS", UtilsValidator.get(jsonNode, "missingkey"), "something")).isFalse();
    }

    @Test
    public void in_OK() {
        String test = "{\"key1\": \"toto\"}";
        JsonNode jsonNode = createJsonNode(test);
        assertThat(evaluate("IN", UtilsValidator.get(jsonNode, "key1"), "titi", "toto", "tata")).isTrue();
    }

    @Test
    public void in_KO() {
        String test = "{\"key1\": \"blablablabla toto blablabla\"}";
        JsonNode jsonNode = createJsonNode(test);
        assertThat(evaluate("IN", UtilsValidator.get(jsonNode, "key1"), "titi")).isFalse();
        assertThat(evaluate("IN", UtilsValidator.get(jsonNode, "missingkey"), "something")).isFalse();
    }


    @Test
    public void isBlank_OK() {
        String test = "{\"key1\": \"blablablabla toto blablabla\", \"key2\": \"\",\"key4\": \"titi\" }";
        JsonNode jsonNode = createJsonNode(test);
        assertThat(evaluate("IS_BLANK", (String) UtilsValidator.get(jsonNode, "key2"))).isTrue();
        assertThat(evaluate("IS_BLANK", (String) UtilsValidator.get(jsonNode, "key3"))).isTrue();
        assertThat(evaluate("IS_BLANK", (String) UtilsValidator.get(jsonNode, "missingkey"))).isTrue();
    }

    @Test
    public void isBlank_KO() {
        String test = "{\"key1\": \"blablablabla toto blablabla\", \"key2\": \"\",\"key4\": \"titi\" }";
        JsonNode JsonNode = createJsonNode(test);
        assertThat(evaluate("IS_BLANK", (Object) UtilsValidator.get(JsonNode, "key1"))).isFalse();
    }

    @Test
    public void isNotBlank_OK() {
        String test = "{\"key1\": \"blablablabla toto blablabla\", \"key2\": \"\",\"key4\": \"titi\" }";
        JsonNode jsonNode = createJsonNode(test);
        assertThat(evaluate("IS_NOT_BLANK", (Object) UtilsValidator.get(jsonNode, "key1"))).isTrue();
        assertThat(evaluate("IS_NOT_BLANK", (Object) UtilsValidator.get(jsonNode, "key4"))).isTrue();
    }

    @Test
    public void isNotBlank_KO() {
        String test = "{\"key1\": \"blablablabla toto blablabla\", \"key2\": \"\",\"key4\": \"titi\" }";
        JsonNode jsonNode = createJsonNode(test);
        assertThat(evaluate("IS_NOT_BLANK", (Object) UtilsValidator.get(jsonNode, "key1"))).isTrue();
        assertThat(evaluate("IS_NOT_BLANK", (Object) UtilsValidator.get(jsonNode, "missingkey"))).isFalse();
    }

    private boolean evaluate(String functionName, Object... args) {
        return FunctionRegistry.getInstance().evaluate(functionName, args);
    }


}