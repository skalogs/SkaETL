package io.skalogs.skaetl.domain;

import lombok.*;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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
    private boolean validateInThePast;
    private int unitInThePast;
    private ChronoUnit chronoUnitInThePast;
    private boolean validateAfterFixedDate;
    private Date lowerFixedDate;
    private boolean validateInFuture;
    private int unitInFuture;
    private ChronoUnit chronoUnitInFuture;
}
