package io.skalogs.skaetl.domain.stat;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StatProcess {
    private String name;
    private String status;
    private Long nbRead;
    private Long nbOutput;
}
