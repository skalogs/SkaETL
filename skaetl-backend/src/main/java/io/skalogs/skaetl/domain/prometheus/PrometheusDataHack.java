package io.skalogs.skaetl.domain.prometheus;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PrometheusDataHack {
    private String status;
    private DataPrometheusHack data;
}
