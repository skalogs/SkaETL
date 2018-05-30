package io.skalogs.skaetl.domain;

import lombok.*;
import lombok.experimental.Wither;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Wither
@Getter
@Setter
public class FilterResult {
    private Boolean filter;
    private ProcessFilter processFilter;
}
