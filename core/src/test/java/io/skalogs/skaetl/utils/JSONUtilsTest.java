package io.skalogs.skaetl.utils;

/*-
 * #%L
 * core
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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class JSONUtilsTest {

    JSONUtils jsonUtils = JSONUtils.getInstance();

    @Test
    public void put() {
        String input = "{ \"name\":\"John\", \"age\":30}";
        JsonNode parse = jsonUtils.parse(input);

        jsonUtils.put(parse, "car.type", JsonNodeFactory.instance.textNode("test"));

        Assertions.assertThat(parse.at("/car").getNodeType()).isEqualTo(JsonNodeType.OBJECT);
        Assertions.assertThat(parse.at("/car/type").asText()).isEqualTo("test");

    }


    @Test
    public void putMultiLevel() {
        String input = "{ \"name\":\"John\", \"age\":30}";
        JsonNode parse = jsonUtils.parse(input);

        jsonUtils.put(parse, "car.model.name", JsonNodeFactory.instance.textNode("multipla"));

        Assertions.assertThat(parse.at("/car").getNodeType()).isEqualTo(JsonNodeType.OBJECT);
        Assertions.assertThat(parse.at("/car/model").getNodeType()).isEqualTo(JsonNodeType.OBJECT);
        Assertions.assertThat(parse.at("/car/model/name").asText()).isEqualTo("multipla");

    }

    @Test
    public void remove() {
        String input = "{\n" +
                "    \"name\":\"John\",\n" +
                "    \"age\":30,\n" +
                "    \"cars\": {\n" +
                "        \"car1\":\"Ford\",\n" +
                "        \"car2\":\"BMW\",\n" +
                "        \"car3\":\"Fiat\"\n" +
                "    }\n" +
                " }";
        JsonNode parse = jsonUtils.parse(input);

        jsonUtils.remove(parse, "age");

        Assertions.assertThat(parse.at("/age").getNodeType()).isEqualTo(JsonNodeType.MISSING);

        jsonUtils.remove(parse, "cars.car3");

        Assertions.assertThat(parse.at("/cars/cars3").getNodeType()).isEqualTo(JsonNodeType.MISSING);

    }
}
