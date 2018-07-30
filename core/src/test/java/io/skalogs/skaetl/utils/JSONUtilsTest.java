package io.skalogs.skaetl.utils;

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