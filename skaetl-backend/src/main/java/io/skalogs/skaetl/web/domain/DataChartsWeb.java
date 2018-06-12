package io.skalogs.skaetl.web.domain;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataChartsWeb {
    private DataCharts dataProcess;
    @Builder.Default
    private Long numberAllClientConfiguration = new Long(0);
    @Builder.Default
    private Long numberProdClientConfiguration = new Long(0);
    @Builder.Default
    private Long numberErrorClientConfiguration = new Long(0);
}
