package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessTransformation {
    private String id;
    private TypeValidation typeTransformation;
    private ParameterTransformation parameterTransformation;
}
