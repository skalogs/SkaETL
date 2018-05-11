package io.skalogs.skaetl.domain;

import lombok.*;
import lombok.experimental.Wither;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Wither
public class ProcessConsumer extends ProcessDefinition {
    private String idProcess;
    private String name;
    private Date timestamp;
    @Builder.Default
    private ProcessInput processInput = new ProcessInput();
    @Builder.Default
    private List<ProcessParser> processParser = new ArrayList<>();
    @Builder.Default
    private List<ProcessValidation> processValidation = new ArrayList<>();
    @Builder.Default
    private List<ProcessTransformation> processTransformation = new ArrayList<>();
    @Builder.Default
    private List<ProcessFilter> processFilter = new ArrayList<>();
    @Builder.Default
    private List<ProcessOutput> processOutput = new ArrayList<>();
}
