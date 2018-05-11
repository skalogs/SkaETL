package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessParser {
    private TypeParser typeParser;
    private String grokPattern;
    private String schemaCSV;
    private String id;
    @Builder.Default
    private Boolean activeFailForward = false;
    private String failForwardTopic;
}
