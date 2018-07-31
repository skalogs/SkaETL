package io.skalogs.skaetl.domain;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CsvLookupData {
    private String field;
    private String data;
    private Map<String, List<ProcessKeyValue>> map = new HashMap<String, List<ProcessKeyValue>>();
}
