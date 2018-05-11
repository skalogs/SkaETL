package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ProcessFilter;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessFilterWeb {
    private String idProcess;
    private ProcessFilter processFilter;
}