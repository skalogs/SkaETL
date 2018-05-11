package io.skalogs.skaetl.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProcessReferential extends ProcessDefinition {
    private Date timestamp;
    @Builder.Default
    private String idProcess = "";
    @Builder.Default
    private String name = "";
    @Builder.Default
    private String referentialKey = "";
    @Builder.Default
    private List<String> listAssociatedKeys = new ArrayList<>();
    @Builder.Default
    private List<String> listIdProcessConsumer = new ArrayList<>();
    @Builder.Default
    private List<String> listMetadata = new ArrayList<>();
}
