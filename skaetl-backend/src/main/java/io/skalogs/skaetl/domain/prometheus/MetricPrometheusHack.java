package io.skalogs.skaetl.domain.prometheus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MetricPrometheusHack {
    @JsonProperty("__name__")
    private String name;
    private String job;
    private String instance;
}
