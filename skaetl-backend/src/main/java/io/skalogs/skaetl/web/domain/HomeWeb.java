package io.skalogs.skaetl.web.domain;

/*-
 * #%L
 * skaetl-backend
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
    private Long numberProcessTotal = new Long(0);
    @Builder.Default
    private Long numberProcessActive = new Long(0);
    @Builder.Default
    private Long numberProcessDeActive = new Long(0);
    @Builder.Default
    private Long numberProcessError = new Long(0);
    @Builder.Default
    private Long numberProcessInit = new Long(0);
    @Builder.Default
    private Long numberProcessDegraded = new Long(0);
    @Builder.Default
    private Long numberProcessCreation = new Long(0);
    @Builder.Default
    private List<StatProcess> listStatProcess = new ArrayList<>();

    @Builder.Default
    private Long numberWorkerTotal = new Long(0);
    @Builder.Default
    private Long numberWorkerProcess = new Long(0);
    @Builder.Default
    private Long numberWorkerMetric = new Long(0);
    @Builder.Default
    private Long numberWorkerReferential = new Long(0);
    @Builder.Default
    private List<StatWorker> listStatWorker = new ArrayList<>();

    @Builder.Default
    private Long numberMetricTotal = new Long(0);
    @Builder.Default
    private Long numberMetricActive = new Long(0);
    @Builder.Default
    private Long numberMetricDeActive = new Long(0);
    @Builder.Default
    private Long numberMetricError = new Long(0);
    @Builder.Default
    private Long numberMetricInit = new Long(0);
    @Builder.Default
    private Long numberMetricCreation = new Long(0);
    @Builder.Default
    private Long numberMetricDegraded = new Long(0);
    @Builder.Default
    private List<StatMetric> listStatMetric = new ArrayList<>();

    @Builder.Default
    private Long numberReferentialTotal = new Long(0);
    @Builder.Default
    private Long numberReferentialActive = new Long(0);
    @Builder.Default
    private Long numberReferentialDeActive = new Long(0);
    @Builder.Default
    private Long numberReferentialError = new Long(0);
    @Builder.Default
    private Long numberReferentialInit = new Long(0);
    @Builder.Default
    private Long numberReferentialDegraded = new Long(0);
    @Builder.Default
    private Long numberReferentialCreation = new Long(0);
    @Builder.Default
    private List<StatReferential> listStatReferential = new ArrayList<>();

    @Builder.Default
    private Long numberConfigurationTotal = new Long(0);
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
