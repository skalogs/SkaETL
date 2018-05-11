package io.skalogs.skaetl.domain.prometheus;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResultPrometheusHack {
    private MetricPrometheusHack metric;
    private String[][] values;
}
