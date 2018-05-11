package io.skalogs.skaetl.domain;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ConfEsSkalogs {
    private String pipeline;
    private ConfigurationLogstash configurationLogstash;
}
