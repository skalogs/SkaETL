package io.skalogs.skaetl.domain.stat;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StatReferential {
    private String name;
    private String status;
    private Long nbInput;
    private Long nbOutput;
}
