package io.skalogs.skaetl.domain;

import lombok.*;
import lombok.experimental.Wither;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Wither
public class ParserResult {
    private String result;
    @Builder.Default
    private Boolean failParse = false;
    private String messageFailParse;
}
