package io.skalogs.skaetl.domain.stat;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StatWorker {
    private String name;
    private String ip;
    private Long nbProcess;
    private String type;
}
