package io.skalogs.skaetl.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ConfigurationHost {
    public String topic;
    public String host;
    public String codec;
    public String port;
    public TypeInput typeInput;
    public TypeOutput typeOutput;
    public String path;
    public String typeForced;
    public String idHostConfiguration;
    public String tag;
}
