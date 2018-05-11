package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ProcessValidation;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessValidationWeb {
    private String idProcess;
    private ProcessValidation processValidation;
}