package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessFilter {
    private String criteria;
    private String name;
    private String idFilter;
    @Builder.Default
    private Boolean activeFailForward = false;
    private String failForwardTopic;
}
