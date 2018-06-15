package io.skalogs.skaetl.web;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PayloadTopic {
    private Integer nbElemBySlot;
    private Integer nbSlot;
}
