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
public class ConfigurationOutput {
    @Builder.Default
    public String topic = "topicDefault";
    @Builder.Default
    public String host = "hostDefault";
    public String codec;
    @Builder.Default
    public String port= "9092";
    @Builder.Default
    public List<String> listTag = new ArrayList();
}
