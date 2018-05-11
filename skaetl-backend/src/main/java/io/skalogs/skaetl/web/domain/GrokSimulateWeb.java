package io.skalogs.skaetl.web.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GrokSimulateWeb {
    private String grokPattern;
    private String valueList;
}
