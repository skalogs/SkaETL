package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ParameterOutput {
    @Builder.Default
    private String topicOut = "output-topic";
    @Builder.Default
    private RetentionLevel elasticsearchRetentionLevel = RetentionLevel.week;
    @Builder.Default
    private String email = "";
    @Builder.Default
    private String webHookURL = "";
    @Builder.Default
    private String template = "";
}
