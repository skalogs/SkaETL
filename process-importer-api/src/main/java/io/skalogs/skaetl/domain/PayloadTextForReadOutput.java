package io.skalogs.skaetl.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayloadTextForReadOutput {
    private String textSubmit;
    private ProcessConsumer processConsumer;
}
