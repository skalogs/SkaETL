package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ClientLogstash;
import io.skalogs.skaetl.domain.stat.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HomeWeb {
    @Builder.Default
    private Long numberProcessActive = new Long(0);
    @Builder.Default
    private Long numberProcessDeActive = new Long(0);
    @Builder.Default
    private Long numberProcessError = new Long(0);
    @Builder.Default
    private Long numberProcessInit = new Long(0);
    @Builder.Default
    private List<StatProcess> listStatProcess = new ArrayList<>();

    @Builder.Default
    private Long numberWorkerProcess = new Long(0);
    @Builder.Default
    private Long numberWorkerMetric = new Long(0);
    @Builder.Default
    private Long numberWorkerReferential = new Long(0);
    @Builder.Default
    private List<StatWorker> listStatWorker = new ArrayList<>();
    @Builder.Default

    private Long numberMetricActive = new Long(0);
    @Builder.Default
    private Long numberMetricDeActive = new Long(0);
    @Builder.Default
    private Long numberMetricError = new Long(0);
    @Builder.Default
    private Long numberMetricInit = new Long(0);
    @Builder.Default
    private List<StatMetric> listStatMetric = new ArrayList<>();

    @Builder.Default
    private Long numberReferentialActive = new Long(0);
    @Builder.Default
    private Long numberReferentialDeActive = new Long(0);
    @Builder.Default
    private Long numberReferentialError = new Long(0);
    @Builder.Default
    private Long numberReferentialInit = new Long(0);
    @Builder.Default
    private List<StatReferential> listStatReferential = new ArrayList<>();

    @Builder.Default
    private Long numberConfigurationActive = new Long(0);
    @Builder.Default
    private Long numberConfigurationDeActive = new Long(0);
    @Builder.Default
    private Long numberConfigurationError = new Long(0);
    @Builder.Default
    private Long numberConfigurationInit = new Long(0);
    @Builder.Default
    private List<StatConfiguration> listStatConfiguration = new ArrayList<>();

    @Builder.Default
    private List<ClientLogstash> listStatClient = new ArrayList<>();
}
