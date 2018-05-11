package io.skalogs.skaetl.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class GrokTest {
    @Test
    public void test() {
        String value = "my line of log and metrics \n an other line \n and final log";
        String[] tabLine = value.split("\n");

        String value2 = "my line of log and metrics  an other line  and final log";
        String[] tabLine2 = value2.split("\n");

        log.error("" + tabLine.length);
        log.error("" + tabLine2.length);
    }
}
