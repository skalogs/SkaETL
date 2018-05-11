package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "patterns")
public class GrokResult {
    @Builder.Default
    public String messageError = "";
    @Builder.Default
    public String value = "";
    @Builder.Default
    public String pattern = "";
}
