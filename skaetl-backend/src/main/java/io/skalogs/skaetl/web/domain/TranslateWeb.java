package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.Translate;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TranslateWeb {
    public String idConfiguration;
    public Translate translate;
}
