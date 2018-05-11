package io.skalogs.skaetl.domain.prometheus;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DataPrometheusHack {
    private String resultType;
    private List<ResultPrometheusHack> result;
}
