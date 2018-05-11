package io.skalogs.skaetl.domain;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PayloadCaptureData {
    private String bootStrapServers;
    private String maxPollRecords;
    private String pollingTime;
    private String idProcess;
    private String topic;
}
