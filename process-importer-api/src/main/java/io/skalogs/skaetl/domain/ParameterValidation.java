package io.skalogs.skaetl.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ParameterValidation {
    private Boolean formatJson;
    private String mandatory;
    @Builder.Default
    private List<ProcessKeyValue> blackList = new ArrayList<>();
    private Long maxFields;
    private Long maxMessageSize;
    private String fieldExist;
}
