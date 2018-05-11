package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ConfigurationHost;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HostWeb {
    public String idConfiguration;
    public ConfigurationHost configurationHost;
}
