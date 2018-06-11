package io.skalogs.skaetl.domain;

import lombok.*;
import lombok.experimental.Wither;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Wither
public class TopicInfo {
    private String name;
    private Integer partition;
    private Integer replica;
    private Boolean secure;
    private Integer retentionHours;
    private Integer sessionTimeoutSec;
    private Integer connectionTimeoutSec;
    private String bootstrapServers;
    private String cleanupPolicy;
}
