package io.skalogs.skaetl.domain;

import lombok.*;
import lombok.experimental.Wither;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@Wither
public class StatusConsumer {
    private String idProcessConsumer;
    private StatusProcess statusProcess;
    private Date creation;
    private ProcessDefinition processDefinition;
}
