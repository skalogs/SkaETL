package io.skalogs.skaetl.web.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfigurationWeb {
    public String idConfiguration;
    public String name;
    @Builder.Default
    public List<ConfigWeb> input = new ArrayList<>();
    @Builder.Default
    public List<ConfigWeb> output = new ArrayList<>();
    @Builder.Default
    public List<ConfigWeb> translate = new ArrayList<>();
    public Date timestamp;
    public String statusConfig;
}
