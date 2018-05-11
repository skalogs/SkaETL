package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessOutput {
    @Builder.Default
    private ParameterOutput parameterOutput;
    @Builder.Default
    private TypeOutput typeOutput = TypeOutput.ELASTICSEARCH;
    private String id;
}
