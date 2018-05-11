package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ProcessOutput;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessOutputWeb {
    private String idProcess;
    private String id;
    private ProcessOutput processOutput;
}
