package io.skalogs.skaetl.domain;

import lombok.*;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ParameterTransformation {

    @Builder.Default
    private ProcessKeyValue composeField = new ProcessKeyValue();
    @Builder.Default
    private String keyField = "";
    @Builder.Default
    private FormatDateValue formatDateValue = new FormatDateValue();
    @Builder.Default
    private HashMap<String, String> mapLookup = new HashMap<>();
    @Builder.Default
    private ExternalHTTPData externalHTTPData = new ExternalHTTPData();
    @Builder.Default
    private ProcessHashData processHashData = new ProcessHashData();
}

