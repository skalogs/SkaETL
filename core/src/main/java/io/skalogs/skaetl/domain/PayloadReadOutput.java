package io.skalogs.skaetl.domain;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PayloadReadOutput {
    private String bootStrapServers;
    private String maxRecords;
    private String windowTime;
    private String offset;
    private String topic;
    private String deserializer;
}
