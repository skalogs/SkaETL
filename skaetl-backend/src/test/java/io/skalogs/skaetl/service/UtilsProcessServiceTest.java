package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.ProcessKeyValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UtilsProcessServiceTest {
    @Test
    public void should_Ok_OneLine() {
        UtilsProcessService utilsProcessService = new UtilsProcessService();
        String data = "value;key1;value1;key2;value2";
        Map<String, List<ProcessKeyValue>> map = utilsProcessService.computeDataFromCsv(data);
        assertThat(map.get("value").get(0).getKey()).isEqualTo("key1");
        assertThat(map.get("value").get(0).getValue()).isEqualTo("value1");
        assertThat(map.get("value").get(1).getKey()).isEqualTo("key2");
        assertThat(map.get("value").get(1).getValue()).isEqualTo("value2");
    }

    @Test
    public void should_Ok_MultiLine() {
        UtilsProcessService utilsProcessService = new UtilsProcessService();
        String data = "l0;key1;value1;key2;value2\nl1;key1;value1;key2;value2\nl2;key1;value1;key2;value2\nl3;key1;value1;type;typeTodefine\n";
        Map<String, List<ProcessKeyValue>> map = utilsProcessService.computeDataFromCsv(data);
        assertThat(map.get("l0").get(0).getKey()).isEqualTo("key1");
        assertThat(map.get("l0").get(0).getValue()).isEqualTo("value1");
        assertThat(map.get("l3").get(1).getKey()).isEqualTo("type");
        assertThat(map.get("l3").get(1).getValue()).isEqualTo("typeTodefine");
    }

    @Test
    public void should_Ko_OneLine() {
        UtilsProcessService utilsProcessService = new UtilsProcessService();
        String data = "value;key1;key2;value2";
        Map<String, List<ProcessKeyValue>> map = utilsProcessService.computeDataFromCsv(data);
        assertThat(map.size()).isEqualTo(0);
    }

    @Test
    public void should_Ko_OneLine_ManyData() {
        UtilsProcessService utilsProcessService = new UtilsProcessService();
        String data = "value;key1;key2;key2;value2;key3";
        Map<String, List<ProcessKeyValue>> map = utilsProcessService.computeDataFromCsv(data);
        assertThat(map.size()).isEqualTo(0);
    }

    @Test
    public void should_Ko_MultiLine() {
        UtilsProcessService utilsProcessService = new UtilsProcessService();
        String data = "l0;key1;value1;key2;value2\nl1;key1;value1;key2;value2\nl2;key1;value1;key2\nl3;key1;value1;type;typeTodefine\n";
        Map<String, List<ProcessKeyValue>> map = utilsProcessService.computeDataFromCsv(data);
        assertThat(map.size()).isEqualTo(0);
    }

    @Test
    public void should_Ko_Empty() {
        UtilsProcessService utilsProcessService = new UtilsProcessService();
        String data = "";
        Map<String, List<ProcessKeyValue>> map = utilsProcessService.computeDataFromCsv(data);
        assertThat(map.size()).isEqualTo(0);
    }
}
