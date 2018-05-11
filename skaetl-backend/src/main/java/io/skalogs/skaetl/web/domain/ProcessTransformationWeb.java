package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ProcessTransformation;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessTransformationWeb {
    private String idProcess;
    private ProcessTransformation processTransformation;
}
