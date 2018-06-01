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
    @Builder.Default
    private Boolean isNotificationChange = false;
    @Builder.Default
    private String fieldChangeNotification = "";
    @Builder.Default
    private Boolean isValidationTimeAllField = false;
    @Builder.Default
    private Integer timeValidationAllFieldInSec = 0;
    @Builder.Default
    private Boolean isValidationTimeField = false;
    @Builder.Default
    private Integer timeValidationFieldInSec = 0;
    @Builder.Default
    private String fieldChangeValidation = "";
}
