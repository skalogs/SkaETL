package io.skalogs.skaetl.rules;

/*-
 * #%L
 * rule-executor
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skalogs.skaetl.rules.codegeneration.RuleToJava;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsValidatorTest {

    @Test
    public void isGreaterThan_Ok() {
        String test = "{\"key1\": 100}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        //int
        assertThat(UtilsValidator.isGreaterThan(UtilsValidator.get(JsonNode, "key1"), 50)).isTrue();
        //double
        assertThat(UtilsValidator.isGreaterThan(UtilsValidator.get(JsonNode, "key1"), 50.2)).isTrue();
    }

    @Test
    public void isGreaterThan_KO() {
        String test = "{\"key1\": 100}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.isGreaterThan(UtilsValidator.get(JsonNode, "key1"), 150)).isFalse();
        assertThat(UtilsValidator.isGreaterThan(UtilsValidator.get(JsonNode, "missingkey"), 150)).isFalse();
    }

    @Test
    public void isGreaterThanOrEqual_OK() {
        String test = "{\"key1\": 100}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.isGreaterThanOrEqual(UtilsValidator.get(JsonNode, "key1"), 100)).isTrue();
    }

    @Test
    public void isGreaterThanOrEqual_KO() {
        String test = "{\"key1\": 100}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.isGreaterThanOrEqual(UtilsValidator.get(JsonNode, "key1"), 150)).isFalse();
        assertThat(UtilsValidator.isGreaterThanOrEqual(UtilsValidator.get(JsonNode, "missingkey"), 150)).isFalse();
    }

    @Test
    public void isLowerThan_OK() {
        String test = "{\"key1\": 10}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.isLowerThan(UtilsValidator.get(JsonNode, "key1"), 50)).isTrue();
    }

    @Test
    public void isLowerThan_KO() {
        String test = "{\"key1\": 100}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.isLowerThan(UtilsValidator.get(JsonNode, "key1"), 50)).isFalse();
        //missing key
        assertThat(UtilsValidator.isLowerThan(UtilsValidator.get(JsonNode, "key2"), 150)).isFalse();

    }

    @Test
    public void isLowerThanOrEqual_OK() {
        String test = "{\"key1\": 50}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.isLowerThanOrEqual(UtilsValidator.get(JsonNode, "key1"), 50)).isTrue();
    }

    @Test
    public void isLowerThanOrEqual_KO() {
        String test = "{\"key1\": 100}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.isLowerThanOrEqual(UtilsValidator.get(JsonNode, "key1"), 50)).isFalse();
        assertThat(UtilsValidator.isLowerThanOrEqual(UtilsValidator.get(JsonNode, "missingkey"), 150)).isFalse();

    }

    @Test
    public void checkPresent_OK() {
        String test = "{\"key1\": \"blablablabla toto blablabla\", \"key2\": \"tutu\",\"key3\": \"tata\",\"key4\": \"titi\" }";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.checkPresent(JsonNode, "key1", "key2", "key4")).isTrue();
    }

    @Test
    public void checkPresent_KO() {
        String test = "{\"key1\": \"blablablabla toto blablabla\", \"key2\": \"tutu\",\"key3\": \"tata\",\"key4\": \"titi\" }";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        assertThat(UtilsValidator.checkPresent(JsonNode, "key1", "key2", "moi")).isFalse();
    }

    @Test
    public void isDifferent() {
        String test = "{\"key1\": 50, \"key2\": \"something\"}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        //number
        assertThat(UtilsValidator.isDifferentFrom(UtilsValidator.get(JsonNode, "key1"), 5)).isTrue();
        //string
        assertThat(UtilsValidator.isDifferentFrom(UtilsValidator.get(JsonNode, "key2"), "somethingelse")).isTrue();
    }

    @Test
    public void isEqual() {
        String test = "{\"key1\": 50, \"key2\": \"something\"}";
        JsonNode JsonNode = JSONUtils.createJsonNode(test);
        //number
        assertThat(UtilsValidator.isEqualTo(UtilsValidator.get(JsonNode, "key1"), 50)).isTrue();
        //string
        assertThat(UtilsValidator.isEqualTo(UtilsValidator.get(JsonNode, "key2"), "something")).isTrue();
    }

    @Test
    public void testdedebile() throws IOException {
        String test = "{\"key1\": 50, \"key2\": \"something\"}";
        JsonNode jsonNode = JSONUtils.createJsonNode(test);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsString(jsonNode).getBytes();
        JsonNode jsonNode1 = objectMapper.readValue(bytes, JsonNode.class);
        System.out.println(jsonNode1.path("key1").asText());
        System.out.println(jsonNode1.toString());

    }

    @Test
    public void toCamelCase() {
        assertThat(RuleToJava.toCamelCase("my rule name")).isEqualTo("MyRuleName");
        assertThat(RuleToJava.toCamelCase("my_rule_name")).isEqualTo("MyRuleName");
        assertThat(RuleToJava.toCamelCase("épique test")).isEqualTo("EpiqueTest");
    }
}
