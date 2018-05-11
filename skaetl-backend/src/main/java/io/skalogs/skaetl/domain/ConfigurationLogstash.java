package io.skalogs.skaetl.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ConfigurationLogstash {
    public String timestamp;
    @Builder.Default
    public List<ConfigurationHost> input = new ArrayList<>();
    @Builder.Default
    public List<ConfigurationOutput> output = new ArrayList();
    public String name;
    public String idConfiguration;
    public String idEs;
    public StatusConfig statusConfig;
    public ConfData confData;
    @Builder.Default
    public Boolean statusCustomConfiguration =false;
    @Builder.Default
    public String customConfiguration = "";
}
