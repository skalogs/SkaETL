package io.skalogs.skaetl.web.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfigWeb {
    @Builder.Default
    public String id = "";
    @Builder.Default
    public String name = "";
}
