package io.skalogs.skaetl.rules.codegeneration;

import com.fasterxml.jackson.databind.JsonNode;
import io.skalogs.skaetl.rules.codegeneration.filters.RuleFilterToJava;
import io.skalogs.skaetl.rules.filters.GenericFilter;
import io.skalogs.skaetl.rules.filters.RuleFilterExecutor;
import org.junit.Test;

import static io.skalogs.skaetl.rules.JSONUtils.createJsonNode;
import static org.assertj.core.api.Assertions.assertThat;


public class RuleExecutorTest {

    private RuleFilterExecutor ruleExecutor = new RuleFilterExecutor(new RuleFilterToJava());

    @Test
    public void shouldCompile() {
        GenericFilter myCompileFilter = ruleExecutor.instanciate("myCompileFilter", "key1 >= 3");
        String test = "{\"key1\": 100}";
        JsonNode jsonObject = createJsonNode(test);
        assertThat(myCompileFilter.filter(jsonObject)).isTrue();
    }

}