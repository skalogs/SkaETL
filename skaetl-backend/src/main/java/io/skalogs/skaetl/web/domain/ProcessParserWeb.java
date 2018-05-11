package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ProcessParser;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessParserWeb {
    private String idProcess;
    private ProcessParser processParser;
}
