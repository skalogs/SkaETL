package io.skalogs.skaetl.service;

import io.skalogs.skaetl.domain.ProcessKeyValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UtilsProcessService {

    public Map<String, List<ProcessKeyValue>> computeDataFromCsv(String dataFromCsv) {
        HashMap<String, List<ProcessKeyValue>> map = new HashMap<>();
        if (dataFromCsv != null) {
            String[] tabSplitLine = dataFromCsv.split("\n");
            for (String line : tabSplitLine) {
                String[] lineSplitBydelimiter = line.split(";");
                if (lineSplitBydelimiter.length < 3) {
                    return map;
                }
                List<ProcessKeyValue> listElem = new ArrayList<>();
                try {
                    for (int i = 1; i < lineSplitBydelimiter.length; i = i + 2) {
                        listElem.add(ProcessKeyValue.builder().key(lineSplitBydelimiter[i]).value(lineSplitBydelimiter[i + 1]).build());
                    }
                    map.put(lineSplitBydelimiter[0], listElem);
                } catch (Exception e) {
                    log.error("Exception during parse CSV {}", e);
                    return new HashMap<>();
                }
            }
        }
        return map;
    }
}
